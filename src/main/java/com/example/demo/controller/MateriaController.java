package com.example.demo.controller;

import com.example.demo.dto.MateriaDTO;
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MateriaDTO> listarMaterias() {
        return materiaService.getMaterias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> buscarMateriaPorId(@PathVariable Long id) {
        return materiaService.getMateriaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MateriaDTO> criarMateria(@Valid @RequestBody MateriaDTO materiaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.addMateria(materiaDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMateria(@PathVariable Long id) {
        materiaService.deleteMateria(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MateriaDTO atualizarMateria(@PathVariable Long id, @Valid @RequestBody MateriaDTO materiaDTO) {
        return materiaService.updateMateria(id, materiaDTO);
    }

}
