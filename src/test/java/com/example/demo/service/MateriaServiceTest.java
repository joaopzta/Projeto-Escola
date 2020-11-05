package com.example.demo.service;

import com.example.demo.dto.MateriaDTO;
import com.example.demo.dto.mapper.MateriaMapper;
import com.example.demo.model.Materia;
import com.example.demo.repository.MateriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Testes Materia Service")
@ExtendWith(MockitoExtension.class)
public class MateriaServiceTest {

    @Mock
    MateriaRepository materiaRepository;

    @Mock
    MateriaMapper materiaMapper;

    @InjectMocks
    MateriaService materiaService;

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

    @Test
    public void getMateriasTest() {
        when(materiaRepository.findByActive(true)).thenReturn(new ArrayList<>());

        List<MateriaDTO> listaMaterias = this.materiaService.getMaterias();

        assertEquals(new ArrayList<>(), listaMaterias);
    }

    @Test
    public void getMateriaByIdTest() {
        Long id = 1L;
        Materia materia = new Materia(id, "java", true);
        MateriaDTO materiaDTO = new MateriaDTO(id, "java");

        when(materiaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(materia));
        when(materiaMapper.toMateriaDTO(materia)).thenReturn(materiaDTO);

        Optional<MateriaDTO> materiaSalva = this.materiaService.getMateriaById(id);

        assertAll(
                () -> assertTrue(materiaSalva.isPresent()),
                () -> assertEquals(materiaDTO.getId(), materiaSalva.get().getId()),
                () -> assertEquals(materiaDTO.getDescricao(), materiaSalva.get().getDescricao())
        );
    }

    @Test
    public void addMateriaTest() {
        Long id = 1L;
        Materia materia = new Materia(id, "java", true);
        MateriaDTO materiaDTO = new MateriaDTO(id, "java");

        when(materiaMapper.toMateria(materiaDTO)).thenReturn(materia);
        when(materiaMapper.toMateriaDTO(materia)).thenReturn(materiaDTO);
        when(materiaRepository.save(materia)).thenReturn(materia);

        MateriaDTO materiaSalva = this.materiaService.addMateria(materiaDTO);

        assertAll(
                () -> assertEquals(materiaDTO.getId(), materiaSalva.getId()),
                () -> assertEquals(materiaDTO.getDescricao(), materiaSalva.getDescricao())
        );
    }

    @Test
    public void deleteMateriaTest() {
        Long id = 1L;
        Materia materia = new Materia(id, "java", true);

        when(materiaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(materia));
        when(materiaRepository.save(materia)).thenReturn(materia);

        this.materiaService.deleteMateria(id);

        assertFalse(materia.getActive());
    }

    @Test
    public void updateMateriaTest() {
        Long id = 1L;
        Materia materia = new Materia(id, "java", true);
        MateriaDTO materiaDTO = new MateriaDTO(id, "java");

        when(materiaMapper.toMateriaDTO(materia)).thenReturn(materiaDTO);
        when(materiaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(materia));
        when(materiaRepository.save(materia)).thenReturn(materia);

        MateriaDTO materiaSalva = this.materiaService.updateMateria(id, materiaDTO);

        assertAll(
                () -> assertEquals(materiaDTO.getId(), materiaSalva.getId()),
                () -> assertEquals(materiaDTO.getDescricao(), materiaSalva.getDescricao())
        );
    }

    //    --------------------- [ Cenários Com Exceptions ] ---------------------------- //

    @Test
    public void getMateriaByIndexMateriaNotExistTest() {
        Long id = null;

        when(materiaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> materiaService.getMateriaById(id));
    }

    @Test
    public void addMateriaButBodyIsMissing() {
        assertThrows(NullPointerException.class, () -> materiaService.addMateria(null));
    }

    @Test
    public void addMateriaButFieldIsNull() {
        Long id = 1L;
        MateriaDTO materiaDTO = new MateriaDTO(id, null);

        when(materiaMapper.toMateria(materiaDTO)).thenThrow(new ConstraintViolationException("", null));

        assertThrows(ConstraintViolationException.class, () -> materiaService.addMateria(materiaDTO));
    }

    @Test
    public void deleteMateriaButMateriaDoesNotExistTest() {
        Long id = null;

        when(materiaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> materiaService.deleteMateria(id));
    }

    @Test
    public void updateMateriaNotExistTest() {
        Long id = null;
        MateriaDTO materiaDTO = new MateriaDTO(id, null);

        when(materiaRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> materiaService.updateMateria(id, materiaDTO));
    }

    @Test
    public void updateMateriaButBodyIsMissing() {
        Long id = 1L;

        when(materiaRepository.findByIdAndActive(id, true)).thenThrow(new HttpMessageNotReadableException(""));

        assertThrows(HttpMessageNotReadableException.class, () -> materiaService.updateMateria(id, null));
    }

}
