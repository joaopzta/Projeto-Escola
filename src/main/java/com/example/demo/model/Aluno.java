package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "aluno")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matricula;

    private String nome;
    private  String classe;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "programa_id")
    private Programa programa;

    @OneToMany
    @JoinColumn(name = "nota_id")
    private List<NotaAluno> listaNotaAluno;

}
