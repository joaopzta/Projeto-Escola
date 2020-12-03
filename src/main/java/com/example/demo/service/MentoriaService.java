package com.example.demo.service;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.dto.mapper.MentoriaMapper;
import com.example.demo.model.Mentoria;
import com.example.demo.repository.MentoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentoriaService {

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private MentoriaMapper mentoriaMapper;

    public Page<MentoriaDTO> getMentoria(Pageable pageable) {
        return mentoriaRepository.findByActive(pageable, true).map(mentoriaMapper::toMentoriaDTO);
    }

    public Optional<MentoriaDTO> getMentoriaById(Long id) {
        return mentoriaRepository.findByIdAndActive(id, true)
                .map(mentoriaMapper::toMentoriaDTO);
    }

    public MentoriaDTO addMentoria(MentoriaDTO mentoriaDTO) {
        Mentoria mentoria = mentoriaMapper.toMentoria(mentoriaDTO);
        mentoria.setActive(true);
        return mentoriaMapper.toMentoriaDTO(mentoriaRepository.save(mentoria));
    }

    public void deleteMentoria(Long id) {
        Mentoria mentoria = mentoriaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        mentoria.setActive(false);
        mentoriaRepository.save(mentoria);
    }

    public MentoriaDTO updateMentoria(Long id, MentoriaDTO mentoriaDTO) {
        Mentoria mentoria = mentoriaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(mentoriaDTO, mentoria, "id");
        return mentoriaMapper.toMentoriaDTO(mentoriaRepository.save(mentoria));
    }

}
