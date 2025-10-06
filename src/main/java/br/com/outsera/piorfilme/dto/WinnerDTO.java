package br.com.outsera.piorfilme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnerDTO {
    private Long id;
    private Long year;
    private String title;
    private List<String> studios;
    private List<String> producers;
    private Boolean winner;
}
