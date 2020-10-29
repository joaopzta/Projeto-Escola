package com.example.demo.dto.mapper;

import com.example.demo.dto.ProgramaDTO;
import com.example.demo.model.Programa;

public class ProgramaMapper {

    public static Programa toPrograma(ProgramaDTO programaDTO) {
        Programa programa = new Programa();
        programa.setId(programaDTO.getId());
        programa.setNome(programaDTO.getNome());
        programa.setAno(programaDTO.getAno());

        return programa;
    }

    public static ProgramaDTO toProgramaDTO(Programa programa) {
        return new ProgramaDTO(programa.getId(), programa.getNome(), programa.getAno());
    }

}
