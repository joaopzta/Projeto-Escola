package com.example.demo.dto.mapper;

import com.example.demo.dto.MentorDTO;
import com.example.demo.model.Mentor;

public class MentorMapper {

    public static Mentor toMentor(MentorDTO mentorDTO) {
        Mentor mentor = new Mentor();
        mentor.setId(mentorDTO.getId());
        mentor.setNome(mentorDTO.getNome());
        mentor.setPais(mentorDTO.getPais());

        return mentor;
    }

    public static MentorDTO toMentorDTO(Mentor mentor) {
        return new MentorDTO(mentor.getId(), mentor.getNome(), mentor.getPais());
    }

}
