package br.com.outsera.piorfilme.repository;

import br.com.outsera.piorfilme.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findByNameIgnoreCase(String name);
}
