package com.example.demo.service;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.dto.mapper.MentoriaMapper;
import com.example.demo.model.Mentoria;
import com.example.demo.repository.MentoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentoriaService {

    @Autowired
    private MentoriaRepository mentoriaRepository;

    public List<MentoriaDTO> getMentoria() {
        return mentoriaRepository.findByActive(true)
                .parallelStream()
                .map(MentoriaMapper::toMentoriaDTO)
                .collect(Collectors.toList());
    }

    public Optional<MentoriaDTO> getMentoriaById(Long id) {
        return mentoriaRepository.findByIdAndActive(id, true)
                .map(MentoriaMapper::toMentoriaDTO);
    }

    public MentoriaDTO addMentoria(MentoriaDTO mentoriaDTO) {
        Mentoria mentoria = MentoriaMapper.toMentoria(mentoriaDTO);
        mentoria.setActive(true);
        return MentoriaMapper.toMentoriaDTO(mentoriaRepository.save(mentoria));
    }

    public void deleteMentoria(Long id) {
        MentoriaDTO mentoriaDTO = this.getMentoriaById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        Mentoria mentoria = MentoriaMapper.toMentoria(mentoriaDTO);
        mentoria.setActive(false);
        mentoriaRepository.save(mentoria);
    }

    public MentoriaDTO updateMentoria(Long id, MentoriaDTO mentoriaDTO) {
        Mentoria mentoria = mentoriaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(mentoriaDTO, mentoria, "id");
        return MentoriaMapper.toMentoriaDTO(mentoriaRepository.save(mentoria));
    }

}
