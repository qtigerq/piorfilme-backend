package br.com.outsera.piorfilme.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.outsera.piorfilme.dto.*;
import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.model.Producer;
import br.com.outsera.piorfilme.model.Studio;
import br.com.outsera.piorfilme.repository.MovieRepository;
import br.com.outsera.piorfilme.repository.specification.MovieSpecifications;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<Movie> listByTitle(String title) {
        return movieRepository.findByTitleIgnoreCaseContaining(title);
    }

    public boolean delete(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Movie update(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie with id " + id + " not found"));

        existingMovie.setMovieYear(movie.getMovieYear());
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setStudios(movie.getStudios());
        existingMovie.setProducers(movie.getProducers());
        existingMovie.setWinner(movie.getWinner());

        return movieRepository.save(existingMovie);
    }

    public Page<Movie> getPaginatedMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> getPaginatedMovies(Pageable pageable, String title, String movieYear, Boolean winner) {
        if ((title == null || title.isBlank()) && (movieYear == null || movieYear.isBlank()) && winner == null) {
            return movieRepository.findAll(pageable);
        }
        Specification<Movie> filters = MovieSpecifications.withFilters(title, movieYear, winner);
        return movieRepository.findAll(filters, pageable);
    }

    public YearWinnersCountDTO getYearsWithMultipleWinners() {
        List<Object[]> rows = movieRepository.findYearsWithMultipleWinners();

        List<YearWinnersCountItemDTO> items = rows.stream()
                .map(r -> new YearWinnersCountItemDTO((Long) r[0], (Long) r[1]))
                .collect(Collectors.toList());

        return new YearWinnersCountDTO(items);
    }

    public StudiosWithWinCountDTO getStudiosWithWinCount() {
        List<Object[]> rows = movieRepository.findStudiosWinCounts();

        List<StudiosWithWinCountItemDTO> items = rows.stream()
                .map(r -> new StudiosWithWinCountItemDTO((String) r[0], (Long) r[1]))
                .collect(Collectors.toList());

        return new StudiosWithWinCountDTO(items);
    }

    public WinIntervalDTO getMaxMinWinIntervalForProducers() {
        List<Object[]> rows = movieRepository.findProducerWinIntervals();

        List<ProducerWinIntervalDTO> intervals = rows.stream()
                .filter(r -> r[1] != null)
                .map(r -> new ProducerWinIntervalDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue(),
                        ((Number) r[3]).longValue()
                )).toList();

        if (intervals.isEmpty()) {
            return new WinIntervalDTO(List.of(), List.of());
        }

        long min = intervals.stream().mapToLong(ProducerWinIntervalDTO::getInterval).min().orElse(0);
        long max = intervals.stream().mapToLong(ProducerWinIntervalDTO::getInterval).max().orElse(0);

        List<ProducerWinIntervalDTO> minList = intervals.stream().filter(i -> i.getInterval() == min).toList();
        List<ProducerWinIntervalDTO> maxList = intervals.stream().filter(i -> i.getInterval() == max).toList();

        return new WinIntervalDTO(minList, maxList);
    }

    public WinnersByYearDTO getWinnersByYear(String year) {
        if (year == null || year.isBlank()) {
            return new WinnersByYearDTO(new ArrayList<>());
        }

        Long parsedYear = Long.valueOf(year);
        List<Movie> movies = movieRepository.findByMovieYearAndWinnerTrue(parsedYear);
        return new WinnersByYearDTO(moviesToWinnerDTO(movies));
    }

    public List<WinnerDTO> moviesToWinnerDTO(List<Movie> movies) {
        return movies.stream().map(this::movieToWinnerDTO).collect(Collectors.toList());
    }

    public WinnerDTO movieToWinnerDTO(Movie movie) {
        WinnerDTO dto = new WinnerDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getMovieYear());
        dto.setWinner(movie.getWinner());
        dto.setProducers(movie.getProducers().stream().map(Producer::getName).toList());
        dto.setStudios(movie.getStudios().stream().map(Studio::getName).toList());
        return dto;
    }

}
