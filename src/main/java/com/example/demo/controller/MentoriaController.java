package com.example.demo.controller;

import com.example.demo.dto.MentoriaDTO;
import com.example.demo.service.MentoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mentoria")
public class MentoriaController {

    @Autowired
    private MentoriaService mentoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MentoriaDTO> listarMentorias() {
        return mentoriaService.getMentoria();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentoriaDTO> buscarMentoriaPorId(@PathVariable Long id) {
        return mentoriaService.getMentoriaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MentoriaDTO> criarMentoria(@Valid @RequestBody MentoriaDTO mentoriaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentoriaService.addMentoria(mentoriaDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMentoria(@PathVariable Long id) {
        mentoriaService.deleteMentoria(id);
    }

    @PutMapping("/{id}")
    public MentoriaDTO atualizarMentoria(@PathVariable Long id, @Valid @RequestBody MentoriaDTO mentoriaDTO) {
        return mentoriaService.updateMentoria(id, mentoriaDTO);
    }

}
