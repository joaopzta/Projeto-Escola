package com.example.demo.repository;

import com.example.demo.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByActive(Boolean active);
    Optional<Materia> findByIdAndActive(Long id, Boolean active);
}
