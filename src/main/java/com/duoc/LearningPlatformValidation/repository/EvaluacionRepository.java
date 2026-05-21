package com.duoc.LearningPlatformValidation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByCursoId(Long cursoId);
}
