package br.com.outsera.piorfilme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudiosWithWinCountDTO {
    private List<StudiosWithWinCountItemDTO> studios;
}
