package br.com.outsera.piorfilme.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import org.springframework.stereotype.Repository;

import br.com.outsera.piorfilme.model.Movie;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findAllWinnerMovies() {
        return entityManager.createNativeQuery(
            "SELECT * FROM movie WHERE winner = true",
            Movie.class
        ).getResultList();
    }

    @Override
    public List<Object[]> findYearsWithMultipleWinners() {
        return entityManager.createNativeQuery(
            """
            SELECT m.movie_year, COUNT(*) AS win_count
            FROM movie m
            WHERE m.winner = true
            GROUP BY m.movie_year
            HAVING COUNT(*) > 1
            ORDER BY m.movie_year
            """,
            Object[].class
        ).getResultList();
    }

    @Override
    public List<Object[]> findStudiosWinCounts() {
        return entityManager.createNativeQuery(
            """
            SELECT s.name AS studio, COUNT(*) AS win_count
            FROM movie m
            JOIN movie_studio ms ON ms.movie_id = m.id
            JOIN studio s ON s.id = ms.studio_id
            WHERE m.winner = true
            GROUP BY s.name
            ORDER BY win_count DESC
            """,
            Object[].class
        ).getResultList();
    }

    @Override
    public List<Object[]> findProducerWinIntervals() {
        return entityManager.createNativeQuery(
            """
            SELECT 
                p.name AS producer,
                (m.movie_year - LAG(m.movie_year) OVER (PARTITION BY p.name ORDER BY m.movie_year)) AS win_interval,
                LAG(m.movie_year) OVER (PARTITION BY p.name ORDER BY m.movie_year) AS previous_win,
                m.movie_year AS following_win
            FROM movie m
            JOIN movie_producer mp ON mp.movie_id = m.id
            JOIN producer p ON p.id = mp.producer_id
            WHERE m.winner = true
            ORDER BY p.name, m.movie_year
            """,
            Object[].class
        ).getResultList();
    }

}