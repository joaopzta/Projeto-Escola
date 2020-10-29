package com.example.demo.service;

import com.example.demo.dto.MentorDTO;
import com.example.demo.dto.mapper.MentorMapper;
import com.example.demo.dto.mapper.MentoriaMapper;
import com.example.demo.model.Mentor;
import com.example.demo.model.Mentoria;
import com.example.demo.repository.MentorRepository;
import com.example.demo.repository.MentoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private MentorMapper mentorMapper;

    public List<MentorDTO> getMentores() {
        return mentorRepository.findByActive(true)
                .parallelStream()
                .map(mentorMapper::toMentorDTO)
                .collect(Collectors.toList());
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

    public void deleteMentor(Long id) {
        MentorDTO mentorDTO = this.getMentorById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        Mentor mentor = mentorMapper.toMentor(mentorDTO);
        mentor.setActive(false);
        mentorRepository.save(mentor);
        List<Mentoria> listaMentoria = mentoriaRepository.findByActive(true);
        listaMentoria.parallelStream().filter(mentoria -> !mentoria.getMentor().getActive()).forEach(mentoria -> {
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



