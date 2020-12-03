package com.example.demo.service;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.dto.mapper.MentoriaMapper;
import com.example.demo.model.Aluno;
import com.example.demo.model.Mentor;
import com.example.demo.model.Mentoria;
import com.example.demo.repository.MentoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Testes Mentoria Service")
@ExtendWith(MockitoExtension.class)
public class MentoriaServiceTest {

    @Mock
    MentoriaRepository mentoriaRepository;

    @Mock
    MentoriaMapper mentoriaMapper;

    @InjectMocks
    MentoriaService mentoriaService;

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

//    @Test
//    public void getMentoriasTest() {
//        when(mentoriaRepository.findByActive(true)).thenReturn(new ArrayList<>());
//
//        List<MentoriaDTO> listaMentorias = this.mentoriaService.getMentoria();
//
//        assertEquals(new ArrayList<>(), listaMentorias);
//    }

    @Test
    public void getMentoriaByIdTest() {
        Long id = 1L;
        Mentoria mentoria = new Mentoria(id, new Aluno(), new Mentor(), true);
        MentoriaDTO mentoriaDTO = new MentoriaDTO(id, new Aluno(), new Mentor());

        when(mentoriaMapper.toMentoriaDTO(mentoria)).thenReturn(mentoriaDTO);
        when(mentoriaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(mentoria));

        Optional<MentoriaDTO> mentoriaSalva = this.mentoriaService.getMentoriaById(id);

        assertAll(
                () -> assertTrue(mentoriaSalva.isPresent()),
                () -> assertEquals(mentoriaDTO.getId(), mentoriaSalva.get().getId()),
                () -> assertEquals(mentoriaDTO.getAluno(), mentoriaSalva.get().getAluno()),
                () -> assertEquals(mentoriaDTO.getMentor(), mentoriaSalva.get().getMentor())
        );
    }

    @Test
    public void addMentoriaTest() {
        Long id = 1L;
        Mentoria mentoria = new Mentoria(id, new Aluno(), new Mentor(), true);
        MentoriaDTO mentoriaDTO = new MentoriaDTO(id, new Aluno(), new Mentor());

        when(mentoriaMapper.toMentoriaDTO(mentoria)).thenReturn(mentoriaDTO);
        when(mentoriaMapper.toMentoria(mentoriaDTO)).thenReturn(mentoria);
        when(mentoriaRepository.save(mentoria)).thenReturn(mentoria);

        MentoriaDTO mentoriaSalva = this.mentoriaService.addMentoria(mentoriaDTO);

        assertAll(
                () -> assertEquals(mentoriaDTO.getId(), mentoriaSalva.getId()),
                () -> assertEquals(mentoriaDTO.getAluno(), mentoriaSalva.getAluno()),
                () -> assertEquals(mentoriaDTO.getMentor(), mentoriaSalva.getMentor())
        );
    }

    @Test
    public void deleteMentoriaTest() {
        Long id = 1L;
        Mentoria mentoria = new Mentoria(id, new Aluno(), new Mentor(), true);

        when(mentoriaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(mentoria));

        this.mentoriaService.deleteMentoria(id);

        assertFalse(mentoria.getActive());
    }

    @Test
    public void updateMentoriaTest() {
        Long id = 1L;
        Mentoria mentoria = new Mentoria(id, new Aluno(), new Mentor(), true);
        MentoriaDTO mentoriaDTO = new MentoriaDTO(id, new Aluno(), new Mentor());

        when(mentoriaMapper.toMentoriaDTO(mentoria)).thenReturn(mentoriaDTO);
        when(mentoriaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(mentoria));
        when(mentoriaRepository.save(mentoria)).thenReturn(mentoria);

        MentoriaDTO mentoriaSalva = this.mentoriaService.updateMentoria(id, mentoriaDTO);

        assertAll(
                () -> assertEquals(mentoriaDTO.getId(), mentoriaSalva.getId()),
                () -> assertEquals(mentoriaDTO.getAluno(), mentoriaSalva.getAluno()),
                () -> assertEquals(mentoriaDTO.getMentor(), mentoriaSalva.getMentor())
        );
    }

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

    @Test
    public void getMentoriaByIndexMentoriaNotExistTest() {
        Long id = null;

        when(mentoriaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentoriaService.getMentoriaById(id));
    }

    @Test
    public void addMentoriaButBodyIsMissing() {
        assertThrows(NullPointerException.class, () -> mentoriaService.addMentoria(null));
    }

    @Test
    public void addMentoriaButFieldIsNull() {
        Long id = 1L;
        MentoriaDTO mentoriaDTO = new MentoriaDTO(id, null, new Mentor());

        when(mentoriaMapper.toMentoria(mentoriaDTO)).thenThrow(new ConstraintViolationException("", null));

        assertThrows(ConstraintViolationException.class, () -> mentoriaService.addMentoria(mentoriaDTO));
    }

    @Test
    public void deleteMentoriaButMentoriaDoesNotExistTest() {
        Long id = null;

        when(mentoriaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentoriaService.deleteMentoria(id));
    }

    @Test
    public void updateMentoriaNotExistTest() {
        Long id = null;
        MentoriaDTO mentoriaDTO = new MentoriaDTO(id, new Aluno(), new Mentor());

        when(mentoriaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentoriaService.updateMentoria(id, mentoriaDTO));
    }

    @Test
    public void updateMentoriaButBodyIsMissing() {
        Long id = 1L;

        when(mentoriaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentoriaService.updateMentoria(id, null));
    }

}
