package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.model.*;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.MentoriaRepository;
import com.example.demo.repository.ProgramaRepository;
import org.junit.jupiter.api.Assertions;
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

@DisplayName("Teste Aluno Service")
@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

    @Mock
    ProgramaRepository programaRepository;

    @Mock
    AlunoRepository alunoRepository;

    @Mock
    MentoriaRepository mentoriaRepository;

    @InjectMocks
    AlunoService alunoService;

    @Test
    public void getAlunosTest() {
        Mockito.when(alunoRepository.findByActive(true)).thenReturn(new ArrayList<>());
        List<AlunoDTO> listaAlunoDTO = this.alunoService.getAlunos();

        Assertions.assertEquals(new ArrayList<>(), listaAlunoDTO);
    }

    @Test
    public void getAlunoByIndexTest() {
        Long id = 1L;
        Mockito.when(alunoRepository.findByMatriculaAndActive(id, true)).thenReturn(Optional.of(
                new Aluno(
                        1L,
                        "joao",
                        "3-A",
                        true,
                        new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true),
                        new ArrayList<NotaAluno>()
                )));
        Optional<AlunoDTO> alunoDTO = this.alunoService.getAlunoByIndex(id);

        Assertions.assertTrue(alunoDTO.isPresent());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, alunoDTO.get().getMatricula()),
                () -> Assertions.assertEquals("joao", alunoDTO.get().getNome()),
                () -> Assertions.assertEquals("3-A", alunoDTO.get().getClasse()),
                () -> Assertions.assertEquals(new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), alunoDTO.get().getPrograma()),
                () -> Assertions.assertEquals(new ArrayList<NotaAluno>(), alunoDTO.get().getListaNotaAluno())
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
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);
        AlunoDTO alunoDTO = this.alunoService.addAluno(dto);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, alunoDTO.getMatricula()),
                () -> Assertions.assertEquals("joao", alunoDTO.getNome()),
                () -> Assertions.assertEquals("3-A", alunoDTO.getClasse()),
                () -> Assertions.assertEquals(new Programa(id, nome, ano, true), alunoDTO.getPrograma()),
                () -> Assertions.assertNull(alunoDTO.getListaNotaAluno())
        );
    }

    @Test
    public void deleteAlunoTest() {
        Long id = 1L;
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<NotaAluno>());
        Mentoria mentoria = new Mentoria(2L, aluno, new Mentor(), true);
        Mockito.when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);
        Mockito.when(mentoriaRepository.findByActive(true)).thenReturn(new ArrayList<Mentoria>());
        Mockito.when(mentoriaRepository.save(mentoria)).thenReturn(mentoria);
        this.alunoService.deleteAluno(id);

        Mockito.verify(mentoriaRepository, Mockito.atLeastOnce()).save(mentoria);

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, aluno.getMatricula()),
                () -> Assertions.assertEquals("joao", aluno.getNome()),
                () -> Assertions.assertEquals("3-A", aluno.getClasse()),
                () -> Assertions.assertFalse(aluno.getActive()),
                () -> Assertions.assertEquals(new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), aluno.getPrograma()),
                () -> Assertions.assertEquals(new ArrayList<NotaAluno>(), aluno.getListaNotaAluno()),
                () -> Assertions.assertEquals(aluno, mentoria.getAluno()),
                () -> Assertions.assertFalse(mentoria.getActive())
        );
    }

}
