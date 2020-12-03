package com.example.demo.controller;

import com.example.demo.dto.MentorDTO;
import com.example.demo.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mentor")
@CrossOrigin(origins="http://localhost:3000")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<MentorDTO> listarMentores(Pageable pageable) {
        return mentorService.getMentores(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> buscarMentoresById(@PathVariable Long id) {
        return mentorService.getMentorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MentorDTO> criarMentor(@Valid @RequestBody MentorDTO mentorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorService.addMentor(mentorDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMentor(Pageable pageable, @PathVariable Long id) {
        mentorService.deleteMentor(pageable, id);
    }

    @PutMapping("/{id}")
    public MentorDTO atualizarMentor(@PathVariable Long id, @Valid @RequestBody MentorDTO mentorDTO) {
        return mentorService.updateMentor(id, mentorDTO);
    }

}
