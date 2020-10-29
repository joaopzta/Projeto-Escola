package com.example.demo.service;

import com.example.demo.dto.ProgramaDTO;
import com.example.demo.dto.mapper.ProgramaMapper;
import com.example.demo.model.Programa;
import com.example.demo.repository.ProgramaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgramaService {

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private ProgramaMapper programaMapper;

    public List<ProgramaDTO> getProgramas() {
        return programaRepository.findByActive(true)
                .parallelStream()
                .map(programaMapper::toProgramaDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProgramaDTO> getProgramaById(Long id) {
        return programaRepository.findByIdAndActive(id, true)
                .map(programaMapper::toProgramaDTO);
    }

    public ProgramaDTO addPrograma(ProgramaDTO programaDTO) {
        Programa programa = programaMapper.toPrograma(programaDTO);
        programa.setActive(true);
        return programaMapper.toProgramaDTO(programaRepository.save(programa));
    }

    public void deletePrograma(Long id) {
        ProgramaDTO programaDTO = this.getProgramaById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        Programa programa = programaMapper.toPrograma(programaDTO);
        programa.setActive(false);
        programaRepository.save(programa);
    }

    public ProgramaDTO updatePrograma(Long id, ProgramaDTO programaDTO) {
        Programa programaSalvo = programaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(programaDTO, programaSalvo, "id");
        return programaMapper.toProgramaDTO(programaRepository.save(programaSalvo));
    }

}
