package br.com.outsera.piorfilme.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.outsera.piorfilme.dto.StudiosWithWinCountDTO;
import br.com.outsera.piorfilme.dto.WinIntervalDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountDTO;
import br.com.outsera.piorfilme.model.Movie;
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

    public WinIntervalDTO findProducerWinInterval() {
        return movieRepository.findProducerWinInterval();
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

}
