package com.example.demo.repository;

import com.example.demo.model.NotaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaAlunoRepository extends JpaRepository<NotaAluno, Long> {

}
