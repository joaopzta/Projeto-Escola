package com.example.demo.dto.mapper;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.model.Mentoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentoriaMapper {

    Mentoria toMentoria(MentoriaDTO mentoriaDTO);
    MentoriaDTO toMentoriaDTO(Mentoria mentoria);

}
