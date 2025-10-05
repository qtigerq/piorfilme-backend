package br.com.outsera.piorfilme.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import br.com.outsera.piorfilme.bootstrap.CSVLoader;
import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.repository.MovieRepository;

@Service
public class CSVImportService {

    private static final Logger log = LoggerFactory.getLogger(CSVLoader.class);

    @Autowired
    private MovieRepository movieRepository;

    public void CSVImport(Resource CSVFile) {
        log.info("##### Iniciando carregamento do CSV...");

        try {
            List<Movie> movies = this.loadCSVFile(CSVFile);

            movieRepository.saveAll(movies);

            log.info("##### CSV carregado com sucesso.");

        } catch (Exception e) {
            log.error("##### Erro ao carregar o CSV: {}", e.getMessage(), e);
        }
    }

    public List<Movie> loadCSVFile(Resource CSVFile) throws Exception {
        List<Movie> expectedMovies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CSVFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 1 || fields[1].trim().isEmpty()) continue;

                Movie movie = new Movie();
                
                if (fields.length > 0 && !fields[0].trim().isEmpty()) {
                    try {
                        movie.setMovieYear(Long.parseLong(fields[0].trim()));
                    } catch (NumberFormatException e) {
                        movie.setMovieYear(null);
                    }
                } else {
                    movie.setMovieYear(null);
                }

                movie.setTitle(fields[1].trim());

                if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                    movie.setStudios(fields[2].trim());
                } else {
                    movie.setStudios(null);
                }

                if (fields.length > 3 && !fields[3].trim().isEmpty()) {
                    movie.setProducers(fields[3].trim());
                } else {
                    movie.setProducers(null);
                }

                if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                    movie.setWinner("yes".equalsIgnoreCase(fields[4].trim()));
                } else {
                    movie.setWinner(false);
                }

                expectedMovies.add(movie);
            }
        }

        return expectedMovies;
    }
    
}
