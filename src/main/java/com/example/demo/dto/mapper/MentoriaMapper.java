package com.example.demo.dto.mapper;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.model.Mentoria;

public class MentoriaMapper {

    public static Mentoria toMentoria(MentoriaDTO mentoriaDTO) {
        Mentoria mentoria = new Mentoria();
        mentoria.setId(mentoriaDTO.getId());
        mentoria.setAluno(mentoriaDTO.getAluno());
        mentoria.setMentor(mentoriaDTO.getMentor());

        return mentoria;
    }

    public static MentoriaDTO toMentoriaDTO(Mentoria mentoria) {
        return new MentoriaDTO(mentoria.getId(), mentoria.getAluno(), mentoria.getMentor());
    }

}
