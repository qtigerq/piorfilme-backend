package br.com.outsera.piorfilme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnersByYearDTO {
    private List<WinnerDTO> winners;
}
