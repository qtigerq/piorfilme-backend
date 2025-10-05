package br.com.outsera.piorfilme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudiosWithWinCountItemDTO {
    private String name;
    private Long winCount;
}
