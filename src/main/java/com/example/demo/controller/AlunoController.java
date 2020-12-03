package com.example.demo.controller;


import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.NotaAlunoDTO;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/aluno")
@CrossOrigin(origins="http://localhost:3000")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AlunoDTO> listarAlunos(Pageable pageable) {
        return alunoService.getAlunos(pageable);
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
    public void removerAluno(Pageable pageable, @PathVariable Long id) {
        alunoService.deleteAluno(pageable, id);
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
