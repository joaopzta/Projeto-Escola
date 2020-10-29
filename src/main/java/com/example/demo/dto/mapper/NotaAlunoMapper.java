package com.example.demo.dto.mapper;

import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.model.NotaAluno;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotaAlunoMapper {

    NotaAluno toNotaAluno(NotaAlunoDTO notaAlunoDTO);
    NotaAlunoDTO toNotaAlunoDTO(NotaAluno notaAluno);

}
