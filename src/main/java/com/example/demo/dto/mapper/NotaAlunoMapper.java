package com.example.demo.dto.mapper;

import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.model.NotaAluno;

public class NotaAlunoMapper {

    public static NotaAluno toNotaAluno(NotaAlunoDTO notaAlunoDTO) {
        NotaAluno notaAluno = new NotaAluno();
        notaAluno.setNota(notaAlunoDTO.getNota());
        notaAluno.setDataNota(notaAlunoDTO.getDataNota());
        notaAluno.setMateria(notaAluno.getMateria());

        return notaAluno;
    }

    public static NotaAlunoDTO toNotaAlunoDTO(NotaAluno notaAluno) {
        return new NotaAlunoDTO(notaAluno.getNota(), notaAluno.getDataNota(), notaAluno.getMateria());
    }

}
