package com.duoc.LearningPlatformValidation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.InscripcionCurso;

public interface InscripcionCursoRepository extends JpaRepository<InscripcionCurso, Long> {

    List<InscripcionCurso> findByInscripcionId(Long inscripcionId);

    List<InscripcionCurso> findByCursoId(Long cursoId);
}
