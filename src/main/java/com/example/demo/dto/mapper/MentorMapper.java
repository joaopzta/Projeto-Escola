package com.example.demo.dto.mapper;

import com.example.demo.dto.MentorDTO;
import com.example.demo.model.Mentor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentorMapper {
    Mentor toMentor(MentorDTO mentorDTO);
    MentorDTO toMentorDTO(Mentor mentor);
}
