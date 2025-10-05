package br.com.outsera.piorfilme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinIntervalDTO {

    private List<ProducerWinIntervalDTO> min;
    private List<ProducerWinIntervalDTO> max;
    
}
