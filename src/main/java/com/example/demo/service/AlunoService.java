package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.dto.mapper.AlunoMapper;
import com.example.demo.model.Aluno;
import com.example.demo.model.Mentoria;
import com.example.demo.model.NotaAluno;
import com.example.demo.model.Programa;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.MateriaRepository;
import com.example.demo.repository.MentoriaRepository;
import com.example.demo.repository.ProgramaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AlunoMapper alunoMapper;

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private NotaAlunoService notaAlunoService;

    public List<AlunoDTO> getAlunos() {
        return alunoRepository.findByActive(true)
                .parallelStream()
                .map(alunoMapper::toAlunoDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlunoDTO> getAlunoByIndex(Long id) {
        return alunoRepository.findByMatriculaAndActive(id, true)
                .map(alunoMapper::toAlunoDTO);
    }

    public AlunoDTO addAluno(AlunoDTO alunoDTO) {
        Programa programa = programaRepository.findByIdAndActive(alunoDTO.getPrograma().getId(), true).orElse(null);
        Aluno aluno = alunoMapper.toAluno(alunoDTO);
        aluno.setPrograma(programa);
        aluno.setActive(true);
        return alunoMapper.toAlunoDTO(alunoRepository.save(aluno));
    }

    public void deleteAluno(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        aluno.setActive(false);

        aluno.getListaNotaAluno().forEach(nota -> notaAlunoService.deleteNotaAluno(nota.getId()));

        alunoRepository.save(aluno);
        List<Mentoria> listaMentoria = mentoriaRepository.findByActive(true);
        listaMentoria.parallelStream().filter(mentoria -> !mentoria.getAluno().getActive()).forEach(mentoria -> {
            mentoria.setActive(false);
            mentoriaRepository.save(mentoria);
        });
    }

    public AlunoDTO updateAluno(Long id, AlunoDTO alunoDTO) {
        Aluno aluno = alunoRepository.findByMatriculaAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(alunoDTO, aluno, "matricula");
        return alunoMapper.toAlunoDTO(alunoRepository.save(aluno));
    }

    public AlunoDTO addNotaAluno(Long id, NotaAlunoDTO notaAlunoDTO) {
        Aluno aluno = alunoRepository.findByMatriculaAndActive(id, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        aluno.getListaNotaAluno().add(notaAlunoService.addNotaAluno(notaAlunoDTO));
        return alunoMapper.toAlunoDTO(alunoRepository.save(aluno));
    }

    public AlunoDTO updateNotaAluno(Long alunoId, Long notaId, NotaAlunoDTO notaAlunoDTO) {
        Aluno aluno = alunoRepository.findByMatriculaAndActive(alunoId, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        NotaAluno notaAluno = aluno.getListaNotaAluno().get(notaId.intValue());
        notaAlunoService.updateNotaAluno(notaAluno.getId(), notaAlunoDTO);
        return alunoMapper.toAlunoDTO(aluno);
    }

    public void deleteNotaAluno(Long alunoId, Long notaId) {
        Aluno aluno = alunoRepository.findByMatriculaAndActive(alunoId, true)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        NotaAluno notaAluno = aluno.getListaNotaAluno().get(notaId.intValue());
        notaAlunoService.deleteNotaAluno(notaAluno.getId());
    }

}
