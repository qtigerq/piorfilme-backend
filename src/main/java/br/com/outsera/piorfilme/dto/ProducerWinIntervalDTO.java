package br.com.outsera.piorfilme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerWinIntervalDTO {
    private String producer;
    private Long interval;
    private Long previousWin;
    private Long followingWin;

}