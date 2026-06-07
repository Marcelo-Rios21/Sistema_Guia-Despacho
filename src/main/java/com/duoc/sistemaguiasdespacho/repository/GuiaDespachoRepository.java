package com.duoc.sistemaguiasdespacho.repository;

import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {

    boolean existsByNumeroGuia(String numeroGuia);

    Optional<GuiaDespacho> findByNumeroGuia(String numeroGuia);

    List<GuiaDespacho> findByTransportistaIgnoreCase(String transportista);

    List<GuiaDespacho> findByFechaEmision(LocalDate fechaEmision);

    List<GuiaDespacho> findByTransportistaIgnoreCaseAndFechaEmision(String transportista, LocalDate fechaEmision);
}