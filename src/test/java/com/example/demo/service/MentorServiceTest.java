package com.example.demo.service;

import com.example.demo.dto.MentorDTO;
import com.example.demo.dto.mapper.MentorMapper;
import com.example.demo.model.Mentor;
import com.example.demo.repository.MentorRepository;
import com.example.demo.repository.MentoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste Mentor Service")
@ExtendWith(MockitoExtension.class)
public class MentorServiceTest {

    @Mock
    MentorRepository mentorRepository;

    @Mock
    MentorMapper mentorMapper;

    @Mock
    MentoriaRepository mentoriaRepository;

    @InjectMocks
    MentorService mentorService;

    //    --------------------- [ Cenários Ideais ] ---------------------------- //

    @Test
    public void getMentoresTest() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Mentor> mentores = Arrays.asList(new Mentor(), new Mentor());
        Page<Mentor> mentorPage = new PageImpl<>(mentores);

        Mockito.when(mentorRepository.findByActive(pageable, true)).thenReturn(mentorPage);

        Page<MentorDTO> listaMentoresDTO = this.mentorService.getMentores(pageable);

        assertEquals(mentorPage.map(mentorMapper::toMentorDTO), listaMentoresDTO);
    }

    @Test
    public void getMentoresByIdTest() {
        Mentor mentor = new Mentor(1L, "marcelo", "brasil", true);
        MentorDTO mentorDTO = new MentorDTO(1L, "marcelo", "brasil");

        Mockito.when(mentorRepository.findByIdAndActive(1L, true)).thenReturn(Optional.of(mentor));
        Mockito.when(mentorMapper.toMentorDTO(mentor)).thenReturn(mentorDTO);

        Optional<MentorDTO> mentorSalvo = this.mentorService.getMentorById(1L);

        assertTrue(mentorSalvo.isPresent());
        assertAll(
                () -> assertEquals(mentorDTO.getId(), mentorSalvo.get().getId()),
                () -> assertEquals(mentorDTO.getNome(), mentorSalvo.get().getNome()),
                () -> assertEquals(mentorDTO.getPais(), mentorSalvo.get().getPais())
        );
    }

    @Test
    public void addMentor() {
        Long id = 1L;
        Mentor mentor = new Mentor(id, "marcelo", "brasil", true);
        MentorDTO mentorDTO = new MentorDTO(id, "marcelo", "brasil");

        Mockito.when(mentorMapper.toMentor(mentorDTO)).thenReturn(mentor);
        Mockito.when(mentorMapper.toMentorDTO(mentor)).thenReturn(mentorDTO);
        Mockito.when(mentorRepository.save(mentor)).thenReturn(mentor);

        MentorDTO mentorSalvo = this.mentorService.addMentor(mentorDTO);

        assertAll(
                () -> assertEquals(mentorDTO.getId(), mentorSalvo.getId()),
                () -> assertEquals(mentorDTO.getNome(), mentorSalvo.getNome()),
                () -> assertEquals(mentorDTO.getPais(), mentorSalvo.getPais())
        );
    }

//    @Test
//    public void deleteMentorTest() {
//        Long id = 1L;
//        Mentor mentor = new Mentor(id, "marcelo", "brasil", true);
//        MentorDTO mentorDTO = new MentorDTO(id, "marcelo", "brasil");
//
//        Mockito.when(mentorMapper.toMentor(mentorDTO)).thenReturn(mentor);
//        Mockito.when(mentorMapper.toMentorDTO(mentor)).thenReturn(mentorDTO);
//        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(mentor));
//        Mockito.when(mentorRepository.save(mentor)).thenReturn(mentor);
//
//        this.mentorService.deleteMentor(id);
//
//        Mockito.verify(mentoriaRepository, Mockito.times(1)).findByActive(true);
//
//        assertFalse(mentor.getActive());
//    }

    @Test
    public void updateMentorTest() {
        Long id = 1L;
        Mentor mentor = new Mentor(id, "marcelo", "brasil", true);
        MentorDTO mentorDTO = new MentorDTO(id, "marcelo", "brasil");

        Mockito.when(mentorMapper.toMentorDTO(mentor)).thenReturn(mentorDTO);
        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(mentor));
        Mockito.when(mentorRepository.save(mentor)).thenReturn(mentor);

        MentorDTO mentorSalvo = this.mentorService.updateMentor(id, mentorDTO);

        assertAll(
                () -> assertEquals(mentorDTO.getId(), mentorSalvo.getId()),
                () -> assertEquals(mentorDTO.getNome(), mentorSalvo.getNome()),
                () -> assertEquals(mentorDTO.getPais(), mentorSalvo.getPais())
        );
    }

    //    --------------------- [ Cenários Com Exceptions ] ---------------------------- //

    @Test
    public void getMentorByIndexMentorNotExistTest() {
        Long id = null;

        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertNull(id);
        assertThrows(EmptyResultDataAccessException.class, () -> mentorService.getMentorById(id));
    }

    @Test
    public void addMentorButBodyIsMissing() {
        assertThrows(NullPointerException.class, () -> mentorService.addMentor(null));
    }

    @Test
    public void addMentorButFieldIsNull() {
        Long id = 1L;
        MentorDTO mentorDTO = new MentorDTO(id, null, "brasil");

        Mockito.when(mentorMapper.toMentor(mentorDTO)).thenThrow(new ConstraintViolationException("", null));

        assertThrows(ConstraintViolationException.class, () -> mentorService.addMentor(mentorDTO));
    }

    @Test
    public void deleteMentorButMentorDoesNotExistTest() {
        Long id = null;
        Pageable pageable = PageRequest.of(0,1);

        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentorService.deleteMentor(pageable, id));
    }

    @Test
    public void updateMentorNotExistTest() {
        Long id = null;
        MentorDTO mentorDTO = new MentorDTO(id, "marcelo", "brasil");

        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(EmptyResultDataAccessException.class, () -> mentorService.updateMentor(id, mentorDTO));
    }

    @Test
    public void updateMentorButBodyIsMissing() {
        Long id = 1L;

        Mockito.when(mentorRepository.findByIdAndActive(id, true)).thenThrow(new HttpMessageNotReadableException(""));

        assertThrows(HttpMessageNotReadableException.class, () -> mentorService.updateMentor(id, null));
    }

}
