package com.example.demo.repository;

import com.example.demo.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends PagingAndSortingRepository<Aluno, Long> {
    Page<Aluno> findByActive(Pageable pageable, Boolean active);
    Optional<Aluno> findByMatriculaAndActive(Long matricula, Boolean active);
}
