package br.com.outsera.piorfilme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.outsera.piorfilme.dto.StudiosWithWinCountDTO;
import br.com.outsera.piorfilme.dto.WinIntervalDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountDTO;
import br.com.outsera.piorfilme.model.Movie;
import br.com.outsera.piorfilme.service.MovieService;

@RestController
@RequestMapping("/movie")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/get-producers-win-intervals")
    public WinIntervalDTO findProducerWinInterval() {
        return movieService.findProducerWinInterval();
    }

    @GetMapping
    public Page<Movie> getPaginatedMovies(Pageable pageable, @RequestParam(required = false) String title, @RequestParam(required = false) String movieYear, @RequestParam(required = false) Boolean winner) {
        return movieService.getPaginatedMovies(pageable, title, movieYear, winner);
    }

    @GetMapping("/yearsWithMultipleWinners")
    public YearWinnersCountDTO yearsWithMultipleWinners() {
        return movieService.getYearsWithMultipleWinners();
    }

    @GetMapping("/studiosWithWinCount")
    public StudiosWithWinCountDTO studiosWithWinCount() {
        return movieService.getStudiosWithWinCount();
    }
    
}
