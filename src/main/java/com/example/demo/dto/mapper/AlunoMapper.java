package com.example.demo.dto.mapper;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.model.Aluno;

public class AlunoMapper {
    public static Aluno toAluno(AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno();
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setNome(alunoDTO.getNome());
        aluno.setClasse(alunoDTO.getClasse());
        aluno.setListaNotaAluno(alunoDTO.getListaNotaAluno());

        return aluno;
    }

    public static AlunoDTO toAlunoDTO(Aluno aluno) {
        return new AlunoDTO(aluno.getMatricula(), aluno.getNome(), aluno.getClasse(), aluno.getPrograma(), aluno.getListaNotaAluno());
    }
}
