package br.com.outsera.piorfilme.repository.custom;

import java.util.List;

import br.com.outsera.piorfilme.model.Movie;

public interface CustomMovieRepository {
    List<Movie> findAllWinnerMovies();
    List<Object[]> findStudiosWinCounts();
    List<Object[]> findYearsWithMultipleWinners();
    List<Object[]> findProducerWinIntervals();
}
