package com.duoc.LearningPlatformValidation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByInscripcionId(Long inscripcionId);

    List<Pago> findByEstado(String estado);
}