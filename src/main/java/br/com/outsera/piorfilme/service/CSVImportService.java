package br.com.outsera.piorfilme.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import br.com.outsera.piorfilme.bootstrap.CSVLoader;
import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.model.Producer;
import br.com.outsera.piorfilme.model.Studio;
import br.com.outsera.piorfilme.repository.MovieRepository;
import br.com.outsera.piorfilme.repository.ProducerRepository;
import br.com.outsera.piorfilme.repository.StudioRepository;
import jakarta.transaction.Transactional;

@Service
public class CSVImportService {

    private static final Logger log = LoggerFactory.getLogger(CSVLoader.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Transactional
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
                if (fields.length < 2 || fields[1].trim().isEmpty()) continue;

                Movie movie = new Movie();

                // YEAR
                try {
                    movie.setMovieYear(Long.parseLong(fields[0].trim()));
                } catch (NumberFormatException e) {
                    movie.setMovieYear(null);
                }

                // TITLE
                movie.setTitle(fields[1].trim());

                // STUDIOS
                Set<Studio> studios = new HashSet<>();
                if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                    String[] studioNames = fields[2].split(",");
                    for (String studioName : studioNames) {
                        String trimmedName = studioName.trim();
                        if (!trimmedName.isEmpty()) {
                            Studio studio = studioRepository
                                .findByNameIgnoreCase(trimmedName)
                                .orElseGet(() -> studioRepository.save(new Studio(null, trimmedName)));
                            studios.add(studio);
                        }
                    }
                }
                movie.setStudios(studios);

                // PRODUCeRS
                Set<Producer> producers = new HashSet<>();

                if (fields.length > 3 && !fields[3].trim().isEmpty()) {
                    String[] producerNames = fields[3]
                        .replaceAll("\\s+and\\s+", ",")
                        .split(",");

                    for (String name : producerNames) {
                        String trimmedName = name.trim();
                        if (trimmedName.isEmpty()) continue;

                        Optional<Producer> existing = producerRepository.findByNameIgnoreCase(trimmedName);
                        Producer producer;

                        if (existing.isPresent()) {
                            producer = existing.get();
                        } else {
                            producer = producerRepository.save(new Producer(null, trimmedName));
                        }

                        producers.add(producer);
                    }
                }

                movie.setProducers(producers);

                // WINNER
                movie.setWinner(fields.length > 4 && "yes".equalsIgnoreCase(fields[4].trim()));
                expectedMovies.add(movie);
            }
        }
        return expectedMovies;
    }
}
