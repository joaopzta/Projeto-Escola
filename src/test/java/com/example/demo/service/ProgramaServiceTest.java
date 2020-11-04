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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Programa Service")
@ExtendWith(MockitoExtension.class)
public class ProgramaServiceTest {

    @Mock
    ProgramaRepository programaRepository;

    @Mock
    ProgramaMapper programaMapper;

    @InjectMocks
    ProgramaService programaService;

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

    @Test
    public void getProgramasTest() {
        Mockito.when(programaRepository.findByActive(true)).thenReturn(new ArrayList<>());

        List<ProgramaDTO> listaProgramasDTO = this.programaService.getProgramas();

        assertEquals(new ArrayList<>(), listaProgramasDTO);
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

}
