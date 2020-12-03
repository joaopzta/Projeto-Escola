package com.example.demo.service;

import com.example.demo.dto.MentorDTO;
import com.example.demo.dto.mapper.MentorMapper;
import com.example.demo.model.Mentor;
import com.example.demo.model.Mentoria;
import com.example.demo.repository.MentorRepository;
import com.example.demo.repository.MentoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private MentorMapper mentorMapper;

    public Page<MentorDTO> getMentores(Pageable pageable) {
        return mentorRepository.findByActive(pageable, true).map(mentorMapper::toMentorDTO);
    }

    public Optional<MentorDTO> getMentorById(Long id) {
        return mentorRepository.findByIdAndActive(id, true)
                .map(mentorMapper::toMentorDTO);
    }

    public MentorDTO addMentor(MentorDTO mentorDTO) {
        Mentor mentor = mentorMapper.toMentor(mentorDTO);
        mentor.setActive(true);
        return mentorMapper.toMentorDTO(mentorRepository.save(mentor));
    }

    public void deleteMentor(Pageable pageable,Long id) {
        MentorDTO mentorDTO = this.getMentorById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        Mentor mentor = mentorMapper.toMentor(mentorDTO);
        mentor.setActive(false);
        mentorRepository.save(mentor);
        Page<Mentoria> listaMentoria = mentoriaRepository.findByActive(pageable, true);
        listaMentoria.filter(mentoria -> !mentoria.getMentor().getActive()).forEach(mentoria -> {
            mentoria.setActive(false);
            mentoriaRepository.save(mentoria);
        });
    }

    public MentorDTO updateMentor(Long id, MentorDTO mentorDTO) {
        Mentor mentorSalvo = mentorRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(mentorDTO, mentorSalvo, "id");
        return mentorMapper.toMentorDTO(mentorRepository.save(mentorSalvo));
    }

}



