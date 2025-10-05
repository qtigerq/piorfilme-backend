package br.com.outsera.piorfilme.repository.custom;

import br.com.outsera.piorfilme.dto.StudiosWithWinCountDTO;
import br.com.outsera.piorfilme.dto.WinIntervalDTO;
import br.com.outsera.piorfilme.dto.YearWinnersCountDTO;

public interface CustomMovieRepository {
    WinIntervalDTO findProducerWinInterval();
    YearWinnersCountDTO getYearsWithMultipleWinners();
    StudiosWithWinCountDTO getStudiosWithWinCount();
}
