package com.example.demo.controller;


import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.model.Aluno;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aluno")
@CrossOrigin(origins="http://localhost:3000")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AlunoDTO> listarAlunos() {
        return alunoService.getAlunos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarAlunosById(@PathVariable Long id) {
        return alunoService.getAlunoByIndex(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criarAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.addAluno(alunoDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerAluno(@PathVariable Long id) {
        alunoService.deleteAluno(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoDTO atualizarAluno(@PathVariable Long id, @Valid @RequestBody AlunoDTO alunoDTO) {
        return alunoService.updateAluno(id, alunoDTO);
    }

    @PutMapping("/{id}/nota")
    public AlunoDTO adicionarNotaAluno(@Valid @PathVariable Long id, @RequestBody NotaAlunoDTO notaAlunoDTO) {
        return alunoService.addNotaAluno(id, notaAlunoDTO);
    }

    @PutMapping("/{alunoId}/nota/{notaId}")
    public AlunoDTO atualizarNotaAluno(@PathVariable Long alunoId, @PathVariable Long notaId,@Valid @RequestBody NotaAlunoDTO notaAlunoDTO) {
        return alunoService.updateNotaAluno(alunoId, notaId, notaAlunoDTO);
    }

    @DeleteMapping("/{alunoId}/nota/{notaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerNotaAluno(@PathVariable Long alunoId, @PathVariable Long notaId) {
        alunoService.deleteNotaAluno(alunoId, notaId);
    }

}
