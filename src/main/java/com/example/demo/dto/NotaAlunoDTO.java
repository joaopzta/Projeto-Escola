package com.example.demo.dto;

import com.example.demo.model.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaAlunoDTO {

    private Double nota;
    private LocalDate dataNota;
    private Materia materia;

}
