package com.example.demo.repository;

import com.example.demo.model.Mentoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentoriaRepository extends PagingAndSortingRepository<Mentoria, Long> {
    Page<Mentoria> findByActive(Pageable pageable, Boolean active);
    Optional<Mentoria> findByIdAndActive(Long id, Boolean active);
}
