package br.com.outsera.piorfilme.repository.specification;

import br.com.outsera.piorfilme.model.Movie;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

public class MovieSpecifications {

    public static Specification<Movie> withFilters(String title, String movieYear, Boolean winner) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (title != null && !title.isBlank()) {
                System.out.println("Creating predicate for title filter: " + title);
                predicate = cb.and(predicate,
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (movieYear != null && !movieYear.isBlank()) {
                System.out.println("Creating predicate for movieYear filter: " + movieYear);
                predicate = cb.and(predicate,
                    cb.equal(cb.function("TO_CHAR", String.class, root.get("movieYear")), movieYear));
            }

            if (winner != null) {
                System.out.println("Creating predicate for winner filter: " + winner);
                predicate = cb.and(predicate,
                    cb.equal(root.get("winner"), winner));
            }

            return predicate;
        };
    }

}
