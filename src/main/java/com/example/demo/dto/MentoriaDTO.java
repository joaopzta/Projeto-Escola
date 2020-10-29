package com.example.demo.dto;

import com.example.demo.model.Aluno;
import com.example.demo.model.Mentor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoriaDTO {

    private Long id;
    private Aluno aluno;
    private Mentor mentor;

}
