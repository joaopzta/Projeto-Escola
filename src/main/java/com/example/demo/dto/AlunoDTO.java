package com.example.demo.dto;

import com.example.demo.model.NotaAluno;
import com.example.demo.model.Programa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {

    private Long matricula;
    private String nome;
    private String classe;
    private Programa programa;
    private List<NotaAluno> listaNotaAluno;

}
