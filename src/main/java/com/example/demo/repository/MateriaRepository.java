package com.example.demo.repository;

import com.example.demo.model.Materia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends PagingAndSortingRepository<Materia, Long> {
    Page<Materia> findByActive(Pageable pageable, Boolean active);
    Optional<Materia> findByIdAndActive(Long id, Boolean active);
}
