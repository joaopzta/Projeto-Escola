package com.example.demo.service;

import com.example.demo.dto.ProgramaDTO;
import com.example.demo.dto.mapper.ProgramaMapper;
import com.example.demo.model.Programa;
import com.example.demo.repository.ProgramaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Programa Service")
@ExtendWith(MockitoExtension.class)
public class ProgramaServiceTest {

    @Mock
    ProgramaRepository programaRepository;

    @Spy
    ProgramaMapper programaMapper;

    @InjectMocks
    ProgramaService programaService;

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

    @Test
    public void getProgramasTest() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Programa> programas = Arrays.asList(new Programa(), new Programa());

        Page<Programa> pageProgramas = new PageImpl<>(programas);

        Mockito.when(programaRepository.findByActive(pageable, true)).thenReturn(pageProgramas);
        Mockito.when(programaMapper.toProgramaDTO(new Programa())).thenReturn(new ProgramaDTO());

        Page<ProgramaDTO> listaProgramasDTO = this.programaService.getProgramas(pageable);

        assertEquals(pageProgramas.map(programaMapper::toProgramaDTO), listaProgramasDTO);
    }

    @Test
    public void getProgramaByIdTest() {
        Long id = 1L;
        Programa programa = new Programa(id, "iniders", LocalDate.parse("2020-09-09"), true);
        ProgramaDTO programaDTO = new ProgramaDTO(id, "iniders", LocalDate.parse("2020-09-09"));

        Mockito.when(programaMapper.toProgramaDTO(programa)).thenReturn(programaDTO);
        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(programa));

        Optional<ProgramaDTO> programaSalvo = this.programaService.getProgramaById(id);

        assertTrue(programaSalvo.isPresent());
        assertAll(
                () -> assertEquals(programaDTO.getId(), programaSalvo.get().getId()),
                () -> assertEquals(programaDTO.getNome(), programaSalvo.get().getNome()),
                () -> assertEquals(programaDTO.getAno(), programaSalvo.get().getAno())
        );
    }

    @Test
    public void addProgramaTest() {
        Long id = 1L;
        Programa programa = new Programa(id, "iniders", LocalDate.parse("2020-09-09"), true);
        ProgramaDTO programaDTO = new ProgramaDTO(id, "iniders", LocalDate.parse("2020-09-09"));

        Mockito.when(programaMapper.toProgramaDTO(programa)).thenReturn(programaDTO);
        Mockito.when(programaMapper.toPrograma(programaDTO)).thenReturn(programa);
        Mockito.when(programaRepository.save(programa)).thenReturn(programa);

        ProgramaDTO programaSalvo = this.programaService.addPrograma(programaDTO);

        assertAll(
                () -> assertEquals(programaDTO.getId(), programaSalvo.getId()),
                () -> assertEquals(programaDTO.getNome(), programaSalvo.getNome()),
                () -> assertEquals(programaDTO.getAno(), programaSalvo.getAno())
        );
    }

    @Test
    public void deleteProgramaTest() {
        Long id = 1L;
        Programa programa = new Programa(id, "iniders", LocalDate.parse("2020-09-09"), true);
        ProgramaDTO programaDTO = new ProgramaDTO(id, "iniders", LocalDate.parse("2020-09-09"));

        Mockito.when(programaMapper.toProgramaDTO(programa)).thenReturn(programaDTO);
        Mockito.when(programaMapper.toPrograma(programaDTO)).thenReturn(programa);
        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(programa));
        Mockito.when(programaRepository.save(programa)).thenReturn(programa);

        this.programaService.deletePrograma(id);

        assertFalse(programa.getActive());
    }

    @Test
    public void updateProgramaTest() {
        Long id = 1L;
        Programa programa = new Programa(id, "iniders", LocalDate.parse("2020-09-09"), true);
        ProgramaDTO programaDTO = new ProgramaDTO(id, "iniders", LocalDate.parse("2020-09-09"));

        Mockito.when(programaMapper.toProgramaDTO(programa)).thenReturn(programaDTO);
        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(programa));
        Mockito.when(programaRepository.save(programa)).thenReturn(programa);

        ProgramaDTO programaSalvo = this.programaService.updatePrograma(id, programaDTO);

        assertAll(
                () -> assertEquals(programaDTO.getId(), programaSalvo.getId()),
                () -> assertEquals(programaDTO.getNome(), programaSalvo.getNome()),
                () -> assertEquals(programaDTO.getAno(), programaSalvo.getAno())
        );
    }

    //    --------------------- [ Cenários Com Exceptions ] ---------------------------- //

    @Test
    public void getProgramaByIndexProgramaNotExistTest() {
        Long id = null;

        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> programaService.getProgramaById(id));
    }

    @Test
    public void addProgramaButBodyIsMissing() {
        assertThrows(NullPointerException.class, () -> programaService.addPrograma(null));
    }

    @Test
    public void addProgramaButFieldIsNull() {
        Long id = null;
        ProgramaDTO programaDTO = new ProgramaDTO(id, null, LocalDate.parse("2020-09-09"));

        Mockito.when(programaMapper.toPrograma(programaDTO)).thenThrow(new ConstraintViolationException("", null));

        assertThrows(ConstraintViolationException.class, () -> programaService.addPrograma(programaDTO));
    }

    @Test
    public void deleteProgramaButProgramaDoesNotExistTest() {
        Long id = null;

        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> programaService.deletePrograma(id));
    }

    @Test
    public void updateProgramaNotExistTest() {
        Long id = null;
        ProgramaDTO programaDTO = new ProgramaDTO(id, "marcelo", LocalDate.parse("2020-09-09"));

        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> programaService.updatePrograma(id, programaDTO));
    }

    @Test
    public void updateProgramaButBodyIsMissing() {
        Long id = 1L;

        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenThrow(new HttpMessageNotReadableException(""));

        assertThrows(HttpMessageNotReadableException.class, () -> programaService.updatePrograma(id, null));
    }

//    @Test
//    public void updateProgramaButFieldIsNull() {
//        Long id = 1L;
//        ProgramaDTO programaDTO = new ProgramaDTO(id, null, LocalDate.parse("2020-09-09"));
//
//        Mockito.when(programaMapper.toPrograma(programaDTO)).thenThrow(new ConstraintViolationException("", null));
//
//        assertThrows(ConstraintViolationException.class, () -> programaService.updatePrograma(id, programaDTO));
//    }

}
