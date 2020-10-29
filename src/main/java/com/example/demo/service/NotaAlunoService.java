package com.example.demo.service;

import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.dto.mapper.AlunoMapper;
import com.example.demo.dto.mapper.NotaAlunoMapper;
import com.example.demo.model.Aluno;
import com.example.demo.model.Materia;
import com.example.demo.model.NotaAluno;
import com.example.demo.repository.MateriaRepository;
import com.example.demo.repository.NotaAlunoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class NotaAlunoService {

    @Autowired
    private NotaAlunoRepository notaAlunoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private NotaAlunoMapper notaAlunoMapper;

    public NotaAluno addNotaAluno(NotaAlunoDTO notaAlunoDTO) {
        Materia materia = materiaRepository.findByIdAndActive(notaAlunoDTO.getMateria().getId(), true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        NotaAluno notaAluno = notaAlunoMapper.toNotaAluno(notaAlunoDTO);
        notaAluno.setMateria(materia);
        return notaAlunoRepository.save(notaAluno);
    }

    public void deleteNotaAluno(Long id) {
        NotaAluno notaAluno = notaAlunoRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        notaAlunoRepository.delete(notaAluno);
    }

    public NotaAlunoDTO updateNotaAluno(Long id, NotaAlunoDTO notaAlunoDTO) {
        NotaAluno notaAluno = notaAlunoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(notaAlunoDTO, notaAluno, "id");
        return notaAlunoMapper.toNotaAlunoDTO(notaAlunoRepository.save(notaAluno));
    }
}
