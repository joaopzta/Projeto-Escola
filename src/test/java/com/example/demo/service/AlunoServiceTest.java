package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.dto.mapper.AlunoMapper;
import com.example.demo.model.Aluno;
import com.example.demo.model.Materia;
import com.example.demo.model.NotaAluno;
import com.example.demo.model.Programa;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.MentoriaRepository;
import com.example.demo.repository.ProgramaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Teste Aluno Service")
@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

    @Mock
    ProgramaRepository programaRepository;

    @Mock
    AlunoRepository alunoRepository;

    @Mock
    MentoriaRepository mentoriaRepository;

    @Mock
    AlunoMapper alunoMapper;

    @Mock
    NotaAlunoService notaAlunoService;

    @InjectMocks
    AlunoService alunoService;

//    --------------------- [ Cenários Ideais ] ---------------------------- //

//    @Test
//    public void getAlunosTest() {
//        Mockito.when(alunoRepository.findByActive(true)).thenReturn(new ArrayList<>());
//
//        List<AlunoDTO> listaAlunoDTO = this.alunoService.getAlunos();
//
//        Assertions.assertEquals(new ArrayList<>(), listaAlunoDTO);
//    }

    @Test
    //@BeforeAll
    public void getAlunoByIndexTest() {
        Long id = 1L;

        Aluno aluno = new Aluno(1L, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        AlunoDTO alunoDTO = new AlunoDTO(1L, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());

        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoMapper.toAlunoDTO(aluno)).thenReturn(alunoDTO);

        Optional<AlunoDTO> alunoDTONovo = this.alunoService.getAlunoByIndex(id);

        Assertions.assertTrue(alunoDTONovo.isPresent());

        Assertions.assertAll(
                () -> Assertions.assertEquals(alunoDTO.getMatricula(), alunoDTONovo.get().getMatricula()),
                () -> Assertions.assertEquals(alunoDTO.getNome(), alunoDTONovo.get().getNome()),
                () -> Assertions.assertEquals(alunoDTO.getClasse(), alunoDTONovo.get().getClasse()),
                () -> Assertions.assertEquals(alunoDTO.getPrograma(), alunoDTONovo.get().getPrograma()),
                () -> Assertions.assertEquals(alunoDTO.getListaNotaAluno(), alunoDTONovo.get().getListaNotaAluno())
        );
    }

    @Test
    public void addAlunoTest() {
        Long id = 1L;
        String nome = "INSIDERS";
        LocalDate ano = LocalDate.parse("2020-09-10");

        AlunoDTO dto = new AlunoDTO(1L, "joao", "3-A", new Programa(id, nome, ano, true), null);
        Aluno aluno = new Aluno(1L, "joao", "3-A", true, new Programa(id, nome, ano, true), null);

        Mockito.when(programaRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(new Programa(id, nome, ano, true)));
        Mockito.when(alunoMapper.toAluno(dto)).thenReturn(aluno);
        Mockito.when(alunoMapper.toAlunoDTO(aluno)).thenReturn(dto);
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);

        AlunoDTO alunoDTO = this.alunoService.addAluno(dto);

        Assertions.assertAll(
                () -> Assertions.assertEquals(dto.getMatricula(), alunoDTO.getMatricula()),
                () -> Assertions.assertEquals(dto.getNome(), alunoDTO.getNome()),
                () -> Assertions.assertEquals(dto.getClasse(), alunoDTO.getClasse()),
                () -> Assertions.assertEquals(dto.getPrograma(), alunoDTO.getPrograma()),
                () -> Assertions.assertNull(alunoDTO.getListaNotaAluno())
        );
    }

//    @Test
//    public void deleteAlunoTest() {
//        Long id = 1L;
//        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
//        Mockito.when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));
//        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);
//        Mockito.when(mentoriaRepository.findByActive(true)).thenReturn(new ArrayList<>());
//
//        this.alunoService.deleteAluno(id);
//
//        Mockito.verify(alunoRepository, Mockito.times(1)).findById(id);
//        Mockito.verify(alunoRepository, Mockito.times(1)).save(aluno);
//        Mockito.verify(mentoriaRepository, Mockito.atLeastOnce()).findByActive(true);
//
//        Assertions.assertFalse(aluno.getActive());
//    }

    @Test
    public void updateAlunoTest() {
        var id = 1L;
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        AlunoDTO alunoDTO = new AlunoDTO(id, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoMapper.toAlunoDTO(aluno)).thenReturn(alunoDTO);
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);

        AlunoDTO alunoDTONovo = this.alunoService.updateAluno(id, alunoDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals(alunoDTO.getMatricula(), alunoDTONovo.getMatricula()),
                () -> Assertions.assertEquals(alunoDTO.getNome(), alunoDTONovo.getNome()),
                () -> Assertions.assertEquals(alunoDTO.getClasse(), alunoDTONovo.getClasse()),
                () -> Assertions.assertEquals(alunoDTO.getPrograma(), alunoDTONovo.getPrograma()),
                () -> Assertions.assertEquals(alunoDTO.getListaNotaAluno(), alunoDTONovo.getListaNotaAluno())
        );
    }

    @Test
    public void addNotaAlunoTest() {
        Long id = 1L;
        NotaAlunoDTO notaAlunoDTO = new NotaAlunoDTO(10.0, LocalDate.parse("2020-09-09"), new Materia(id, "Java", true));
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "Java", true));
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        AlunoDTO alunoDTO = new AlunoDTO(id, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        List<NotaAluno> listaNotaAluno = new ArrayList<>();

        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(aluno));
        Mockito.when(notaAlunoService.addNotaAluno(notaAlunoDTO)).thenReturn(notaAluno);
        Mockito.when(alunoMapper.toAlunoDTO(aluno)).thenReturn(alunoDTO);
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);

        alunoDTO.getListaNotaAluno().add(notaAluno);

        AlunoDTO alunoDTONovo = alunoService.addNotaAluno(id, notaAlunoDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals(alunoDTO.getMatricula(), alunoDTONovo.getMatricula()),
                () -> Assertions.assertEquals(alunoDTO.getNome(), alunoDTONovo.getNome()),
                () -> Assertions.assertEquals(alunoDTO.getClasse(), alunoDTONovo.getClasse()),
                () -> Assertions.assertEquals(alunoDTO.getPrograma(), alunoDTONovo.getPrograma()),
                () -> Assertions.assertEquals(alunoDTO.getListaNotaAluno(), alunoDTONovo.getListaNotaAluno())
        );
    }

    @Test
    public void updateNotaAlunoTest() {
        Long id = 1L;
        Long listaId = 0L;
        List<NotaAluno> listNotaAluno = new ArrayList<>();
        NotaAlunoDTO notaAlunoDTO = new NotaAlunoDTO(10.0, LocalDate.parse("2020-09-09"), new Materia(id, "Java", true));
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "Java", true));
        listNotaAluno.add(notaAluno);
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), listNotaAluno);
        AlunoDTO alunoDTO = new AlunoDTO(id, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), listNotaAluno);

        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoMapper.toAlunoDTO(aluno)).thenReturn(alunoDTO);
        Mockito.when(notaAlunoService.updateNotaAluno(notaAluno.getId(), notaAlunoDTO)).thenReturn(notaAlunoDTO);

        AlunoDTO alunoDTONovo = alunoService.updateNotaAluno(id, listaId, notaAlunoDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals(alunoDTO.getMatricula(), alunoDTONovo.getMatricula()),
                () -> Assertions.assertEquals(alunoDTO.getNome(), alunoDTONovo.getNome()),
                () -> Assertions.assertEquals(alunoDTO.getClasse(), alunoDTONovo.getClasse()),
                () -> Assertions.assertEquals(alunoDTO.getPrograma(), alunoDTONovo.getPrograma()),
                () -> Assertions.assertEquals(alunoDTO.getListaNotaAluno(), alunoDTONovo.getListaNotaAluno())
        );
    }

    @Test
    public void deleteNotaAlunoTest() {
        Long id = 1L;
        List<NotaAluno> listNotaAluno = new ArrayList<>();
        NotaAluno notaAluno = new NotaAluno(id, 10.0, LocalDate.parse("2020-09-09"), new Materia(id, "Java", true));
        listNotaAluno.add(notaAluno);
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), listNotaAluno);

        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(aluno));

        alunoService.deleteNotaAluno(id, 0L);

        Mockito.verify(notaAlunoService, Mockito.atLeastOnce()).deleteNotaAluno(notaAluno.getId());
    }

//    --------------------- [ Cenários Com Exceptions ] ---------------------------- //

    @Test
    public void getAlunoByIndexAlunoNotExistTest() {
        Long id = null;

        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));

        Assertions.assertNull(id);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> alunoService.getAlunoByIndex(id));
    }

    @Test
    public void addAlunoButBodyIsMissing() {
        Assertions.assertThrows(NullPointerException.class, () -> alunoService.addAluno(null));
    }

    @Test
    public void addAlunoButFieldIsNull() {
        Long id = 1L;
        AlunoDTO alunoDTO = new AlunoDTO(id, null, "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        Mockito.when(alunoMapper.toAluno(alunoDTO)).thenThrow(new ConstraintViolationException("", null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> alunoService.addAluno(alunoDTO));
    }

//    @Test
//    public void deleteAlunoButAlunoDoesNotExistTest() {
//        Long id = null;
//
//        Mockito.when(alunoRepository.findById(id)).thenThrow(new EmptyResultDataAccessException(1));
//
//        Assertions.assertNull(id);
//        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> alunoService.deleteAluno(id));
//    }

    @Test
    public void updateAlunoNotExistTest() {
        Long id = null;
        AlunoDTO alunoDTO = new AlunoDTO(id, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<>());
        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenThrow(new EmptyResultDataAccessException(1));
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> alunoService.updateAluno(id, alunoDTO));
    }

    @Test
    public void updateAlunoButBodyIsMissing() {
        Long id = 1L;
        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenThrow(new HttpMessageNotReadableException(""));
        Assertions.assertThrows(HttpMessageNotReadableException.class, () -> alunoService.updateAluno(id, null));
    }

}
