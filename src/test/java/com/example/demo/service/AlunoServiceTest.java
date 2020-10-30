package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.mapper.AlunoMapper;
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

    @Mock
    AlunoMapper alunoMapper;

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

        Aluno aluno = new Aluno(1L, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<NotaAluno>());
        AlunoDTO alunoDTO = new AlunoDTO(1L, "joao", "3-A", new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<NotaAluno>());

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

    @Test
    public void deleteAlunoTest() {
        Long id = 1L;
        Aluno aluno = new Aluno(id, "joao", "3-A", true, new Programa(id, "INSIDERS", LocalDate.parse("2020-09-10"), true), new ArrayList<NotaAluno>());
        Mentoria mentoria = new Mentoria(2L, aluno, new Mentor(), true);
        Mockito.when(alunoRepository.findById(id)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);
        Mockito.when(mentoriaRepository.findByActive(true)).thenReturn(new ArrayList<Mentoria>());

        this.alunoService.deleteAluno(id);

        Mockito.verify(alunoRepository, Mockito.times(1)).findById(id);
        Mockito.verify(alunoRepository, Mockito.times(1)).save(aluno);
        Mockito.verify(mentoriaRepository, Mockito.atLeastOnce()).findByActive(true);

        Assertions.assertFalse(aluno.getActive());
    }

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

}
