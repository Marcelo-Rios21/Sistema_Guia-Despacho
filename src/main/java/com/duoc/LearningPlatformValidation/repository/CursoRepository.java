package com.duoc.LearningPlatformValidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}