package com.example.demo.controller;

import com.example.demo.dto.ProgramaDTO;
import com.example.demo.service.ProgramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programa")
public class ProgramaController {

    @Autowired
    private ProgramaService programaService;

    @GetMapping
    public List<ProgramaDTO> listarProgramas() {
        return programaService.getProgramas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaDTO> buscarProgramasPorId(@PathVariable Long id) {
        return programaService.getProgramaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProgramaDTO> criarPrograma(@RequestBody ProgramaDTO programaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programaService.addPrograma(programaDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPrograma(@PathVariable Long id) {
        programaService.deletePrograma(id);
    }

    @PutMapping("/{id}")
    public ProgramaDTO atualizarPrograma(@PathVariable Long id, @RequestBody ProgramaDTO programaDTO) {
        return programaService.updatePrograma(id, programaDTO);
    }

}
