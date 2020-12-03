package com.example.demo.repository;

import com.example.demo.model.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends PagingAndSortingRepository<Mentor, Long> {
    Page<Mentor> findByActive(Pageable pageable, Boolean active);
    Optional<Mentor> findByIdAndActive(Long id, Boolean active);
}
