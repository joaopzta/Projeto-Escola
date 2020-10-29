package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mentoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_matricula")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    private Boolean active;

}
