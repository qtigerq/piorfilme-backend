package br.com.outsera.piorfilme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.outsera.piorfilme.dto.StudiosWithWinCountDTO;
import br.com.outsera.piorfilme.dto.WinIntervalDTO;
import br.com.outsera.piorfilme.dto.WinnerDTO;
import br.com.outsera.piorfilme.dto.WinnersByYearDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountDTO;
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
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie with id " + id + " not found"));

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
        return movieRepository.getYearsWithMultipleWinners();
    }

    public StudiosWithWinCountDTO getStudiosWithWinCount() {
        return movieRepository.getStudiosWithWinCount();
    }

    public WinIntervalDTO getMaxMinWinIntervalForProducers() {
        return movieRepository.getMaxMinWinIntervalForProducers();
    }

    public WinnersByYearDTO getWinnersByYear(String year) {
        if (!year.isEmpty() && !year.isBlank()) {
            Long longYear = Long.valueOf(year);
            List<Movie> movies = movieRepository.findByMovieYearAndWinnerTrue(longYear);

            return new WinnersByYearDTO(this.moviesToWinnerDTO(movies));
        } else {
            return new WinnersByYearDTO(new ArrayList<>());
        }
    }

    public List<WinnerDTO> moviesToWinnerDTO(List<Movie> movies) {
        List<WinnerDTO> winners = new ArrayList<>();
        movies.forEach(movie -> {
            winners.add(this.movieToWinnerDTO(movie));
        });

        return winners;
    }

    public WinnerDTO movieToWinnerDTO(Movie movie) {
        WinnerDTO winnerDTO = new WinnerDTO();
        winnerDTO.setId(movie.getId());
        winnerDTO.setTitle(movie.getTitle());
        winnerDTO.setYear(movie.getMovieYear());
        winnerDTO.setWinner(movie.getWinner());
        winnerDTO.setProducers(movie.getProducers().stream().map(Producer::getName).collect(Collectors.toList()));
        winnerDTO.setStudios(movie.getStudios().stream().map(Studio::getName).collect(Collectors.toList()));
        
        return winnerDTO;
    }

}
