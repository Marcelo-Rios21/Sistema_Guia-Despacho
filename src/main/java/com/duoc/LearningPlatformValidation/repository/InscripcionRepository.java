package com.duoc.LearningPlatformValidation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByCursoId(Long cursoId);
}