package com.example.demo.repository;

import com.example.demo.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByActive(Boolean active);
    Optional<Mentor> findByIdAndActive(Long id, Boolean active);
}
