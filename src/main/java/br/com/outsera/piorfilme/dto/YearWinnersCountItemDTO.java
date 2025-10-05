package br.com.outsera.piorfilme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearWinnersCountItemDTO {
    
    private Long year;

    private Long winnerCount;
}
