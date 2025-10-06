package br.com.outsera.piorfilme.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.outsera.piorfilme.dto.ProducerWinIntervalDTO;
import br.com.outsera.piorfilme.dto.StudiosWithWinCountDTO;
import br.com.outsera.piorfilme.dto.StudiosWithWinCountItemDTO;
import br.com.outsera.piorfilme.dto.WinIntervalDTO;
import br.com.outsera.piorfilme.dto.WinnersByYearDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountItemDTO;
import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.model.Producer;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public WinIntervalDTO getMaxMinWinIntervalForProducers() {

        List<Movie> winners = this.findAllWinnerMovies();
        Map<String, List<Long>> producerWins = this.getYearsOfProducerWins(winners);
        List<ProducerWinIntervalDTO> intervals = this.intervalsBetweenConsecutiveWins(producerWins);

        if (intervals.isEmpty()) {
            return new WinIntervalDTO(Collections.emptyList(), Collections.emptyList());
        }

        return new WinIntervalDTO(this.findMinIntervals(intervals), this.findMaxIntervals(intervals));
    }

    private List<Movie> findAllWinnerMovies() {
        return entityManager.createQuery("SELECT m FROM Movie m WHERE m.winner = true", Movie.class).getResultList();
    }

    private Map<String, List<Long>> getYearsOfProducerWins(List<Movie> winners) {
        Map<String, List<Long>> producerWins = new HashMap<>();

        for (Movie movie : winners) {
            Set<Producer> producers = movie.getProducers();

            for (Producer producer : producers) {
                String producerName = producer.getName();

                producerWins
                    .computeIfAbsent(producerName, k -> new ArrayList<>())
                    .add(movie.getMovieYear());
            }
        }

        return producerWins;
    }

    private List<ProducerWinIntervalDTO> intervalsBetweenConsecutiveWins(Map<String, List<Long>> producerWins) {
        List<ProducerWinIntervalDTO> intervals = new ArrayList<>();

        for (Map.Entry<String, List<Long>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Long> years = entry.getValue();
            Collections.sort(years);

            for (int i = 1; i < years.size(); i++) {
                Long interval = years.get(i) - years.get(i - 1);
                intervals.add(new ProducerWinIntervalDTO(producer, interval, years.get(i - 1), years.get(i)));
            }
        }

        return intervals;
    }

    private List<ProducerWinIntervalDTO> findMinIntervals(List<ProducerWinIntervalDTO> intervals) {
        Long minInterval = intervals.stream().mapToLong(ProducerWinIntervalDTO::getInterval).min().orElse(0);
        
        List<ProducerWinIntervalDTO> minList = intervals.stream()
            .filter(p -> p.getInterval() == minInterval)
            .collect(Collectors.toList());

        return minList;
    }

    private List<ProducerWinIntervalDTO> findMaxIntervals(List<ProducerWinIntervalDTO> intervals) {
        Long maxInterval = intervals.stream().mapToLong(ProducerWinIntervalDTO::getInterval).max().orElse(0);

        List<ProducerWinIntervalDTO> maxList = intervals.stream()
            .filter(p -> p.getInterval() == maxInterval)
            .collect(Collectors.toList());

        return maxList;
    }

    public YearWinnersCountDTO getYearsWithMultipleWinners() {
        List<YearWinnersCountItemDTO> items = entityManager.createQuery(
            "SELECT new br.com.outsera.piorfilme.dto.YearWinnersCountItemDTO(m.movieYear, COUNT(m)) " +
            "FROM Movie m " +
            "WHERE m.winner = true " +
            "GROUP BY m.movieYear " +
            "HAVING COUNT(m) > 1",
            YearWinnersCountItemDTO.class
        ).getResultList();

        YearWinnersCountDTO response = new YearWinnersCountDTO();
        response.setYears(items);
        return response;
    }

    public StudiosWithWinCountDTO getStudiosWithWinCount() {
        List<StudiosWithWinCountItemDTO> items = entityManager.createQuery(
            "SELECT new br.com.outsera.piorfilme.dto.StudiosWithWinCountItemDTO(s.name, COUNT(m)) " +
            "FROM Movie m JOIN m.studios s " +
            "WHERE m.winner = true " +
            "GROUP BY s.name " +
            "ORDER BY COUNT(m) DESC",
            StudiosWithWinCountItemDTO.class
        ).getResultList();

        StudiosWithWinCountDTO response = new StudiosWithWinCountDTO();
        response.setStudios(items);
        return response;
    }

}
