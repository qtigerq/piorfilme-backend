package br.com.outsera.piorfilme.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.repository.custom.CustomMovieRepository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie>, CustomMovieRepository {

    List<Movie> findByTitleIgnoreCaseContaining(String title);
    List<Movie> findByMovieYear(Long movieYear);
    List<Movie> findByStudiosIgnoreCaseContaining(String studios);
    List<Movie> findByProducersIgnoreCaseContaining(String producers);
    List<Movie> findByWinner(Boolean winner);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
