package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "nota_aluno")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double nota;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNota;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;

}
