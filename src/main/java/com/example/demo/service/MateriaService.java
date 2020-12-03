package com.example.demo.service;

import com.example.demo.dto.MateriaDTO;
import com.example.demo.dto.mapper.MateriaMapper;
import com.example.demo.model.Materia;
import com.example.demo.repository.MateriaRepository;
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
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private MateriaMapper materiaMapper;

    public Page<MateriaDTO> getMaterias(Pageable pageable) {
        return materiaRepository.findByActive(pageable, true).map(materiaMapper::toMateriaDTO);
    }

    public Optional<MateriaDTO> getMateriaById(Long id) {
        return materiaRepository.findByIdAndActive(id, true)
                .map(materiaMapper::toMateriaDTO);
    }

    public MateriaDTO addMateria(MateriaDTO materiaDTO) {
        Materia materia = materiaMapper.toMateria(materiaDTO);
        materia.setActive(true);
        return materiaMapper.toMateriaDTO(materiaRepository.save(materia));
    }

    public void deleteMateria(Long id) {
        Materia materia = materiaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        materia.setActive(false);
        materiaRepository.save(materia);
    }

    public MateriaDTO updateMateria(Long id, MateriaDTO materiaDTO) {
        Materia materia = materiaRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(materiaDTO, materia, "id");
        return materiaMapper.toMateriaDTO(materiaRepository.save(materia));
    }

}
