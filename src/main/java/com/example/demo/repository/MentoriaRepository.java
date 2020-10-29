package com.example.demo.repository;

import com.example.demo.model.Mentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {
    List<Mentoria> findByActive(Boolean active);
    Optional<Mentoria> findByIdAndActive(Long id, Boolean active);
}
