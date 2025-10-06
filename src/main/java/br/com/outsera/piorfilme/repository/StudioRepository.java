package br.com.outsera.piorfilme.repository;

import br.com.outsera.piorfilme.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    Optional<Studio> findByNameIgnoreCase(String name);
}
