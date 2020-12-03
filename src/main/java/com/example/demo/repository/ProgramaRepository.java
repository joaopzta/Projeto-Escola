package com.example.demo.repository;

import com.example.demo.model.Programa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramaRepository extends PagingAndSortingRepository<Programa, Long> {
    Page<Programa> findByActive(Pageable pageable, Boolean active);
    Optional<Programa> findByIdAndActive(Long id, Boolean active);
}
