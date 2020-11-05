package com.example.demo.service;

import com.example.demo.dto.MateriaDTO;
import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.dto.mapper.NotaAlunoMapper;
import com.example.demo.model.Materia;
import com.example.demo.model.NotaAluno;
import com.example.demo.repository.MateriaRepository;
import com.example.demo.repository.NotaAlunoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes NotaAluno Service")
@ExtendWith(MockitoExtension.class)
public class NotaAlunoServiceTest {

    @Mock
    NotaAlunoRepository notaAlunoRepository;

    @Mock
    MateriaRepository materiaRepository;

    @Mock
    NotaAlunoMapper notaAlunoMapper;

    @InjectMocks
    NotaAlunoService notaAlunoService;

    @Test
    public void addNotaAlunoTest() {
        Long id = 1L;
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "java", true));
        NotaAlunoDTO notaAlunoDTO = new NotaAlunoDTO(10.0, LocalDate.parse("2020-09-09"), new Materia(id, "java", true));

        when(materiaRepository.findByIdAndActive(notaAluno.getMateria().getId(), true)).thenReturn(Optional.of(new Materia(id, "java", true)));
        when(notaAlunoMapper.toNotaAluno(notaAlunoDTO)).thenReturn(notaAluno);
        when(notaAlunoRepository.save(notaAluno)).thenReturn(notaAluno);

        NotaAluno notaSalva = this.notaAlunoService.addNotaAluno(notaAlunoDTO);

        assertAll(
                () -> assertEquals(notaAluno.getId(), notaSalva.getId()),
                () -> assertEquals(notaAluno.getDataNota(), notaSalva.getDataNota()),
                () -> assertEquals(notaAluno.getNota(), notaSalva.getNota()),
                () -> assertEquals(notaAluno.getMateria(), notaSalva.getMateria())
        );
    }

    @Test
    public void deleteNotaAlunoTest() {
        Long id = 1L;
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "java", true));

        when(notaAlunoRepository.findById(id)).thenReturn(Optional.of(notaAluno));

        this.notaAlunoService.deleteNotaAluno(id);

        verify(notaAlunoRepository, times(1)).delete(notaAluno);
    }

    @Test
    public void updateNotaAlunoTest() {
        Long id = 1L;
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "java", true));
        NotaAlunoDTO notaAlunoDTO = new NotaAlunoDTO(10.0, LocalDate.parse("2020-09-09"), new Materia(id, "java", true));

        when(notaAlunoRepository.findById(id)).thenReturn(Optional.of(notaAluno));
        when(notaAlunoMapper.toNotaAlunoDTO(notaAluno)).thenReturn(notaAlunoDTO);
        when(notaAlunoRepository.save(notaAluno)).thenReturn(notaAluno);

        NotaAlunoDTO notaSalva = this.notaAlunoService.updateNotaAluno(id, notaAlunoDTO);

        assertAll(
                () -> assertEquals(notaAlunoDTO.getDataNota(), notaSalva.getDataNota()),
                () -> assertEquals(notaAlunoDTO.getNota(), notaSalva.getNota()),
                () -> assertEquals(notaAlunoDTO.getMateria(), notaSalva.getMateria())
        );
    }

}
