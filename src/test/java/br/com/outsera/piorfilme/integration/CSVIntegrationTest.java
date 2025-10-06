package br.com.outsera.piorfilme.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.service.CSVImportService;
import br.com.outsera.piorfilme.service.MovieService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CSVIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CSVImportService csvImportService;

    @Test
    public void mustImportAllMovies() throws Exception {
        List<Movie> savedMovies = movieService.findAll();
        assertEquals(206, savedMovies.size());
    }

    @Test
    public void mustContainSavingChristmas2014() throws Exception {
        List<Movie> savedMovies = movieService.findAll();
        boolean found = savedMovies.stream()
            .anyMatch(movie -> movie.getTitle().equals("Saving Christmas") && movie.getMovieYear() == 2014);
        assertTrue(found);
    }

    @Test
    public void mustHaveSameRecords() throws Exception {

        Resource CSVFile = new ClassPathResource("Movielist.csv");
        List<Movie> expectedMovies = this.csvImportService.loadCSVFile(CSVFile);

        List<Movie> savedMovies = movieService.findAll();

        assertEquals(expectedMovies.size(), savedMovies.size());

        for (Movie expected : expectedMovies) {
            boolean match = savedMovies.stream().anyMatch(saved ->
                saved.getTitle().equals(expected.getTitle()) &&
                saved.getMovieYear().equals(expected.getMovieYear()) &&
                saved.getStudios().equals(expected.getStudios()) &&
                saved.getProducers().equals(expected.getProducers()) &&
                saved.getWinner() == expected.getWinner()
            );

            assertTrue(match, "Movie not found: " + expected.getTitle());
        }
    }

}
