package com.example.demo.dto.mapper;

import com.example.demo.dto.ProgramaDTO;
import com.example.demo.model.Programa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramaMapper {

    Programa toPrograma(ProgramaDTO programaDTO);
    ProgramaDTO toProgramaDTO(Programa programa);

}
