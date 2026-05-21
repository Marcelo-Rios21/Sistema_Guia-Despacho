package com.duoc.LearningPlatformValidation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inscripcionId;
    private BigDecimal monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;

    public Pago() {
    }

    public Pago(Long id, Long inscripcionId, BigDecimal monto, String metodoPago, String estado, LocalDateTime fechaPago) {
        this.id = id;
        this.inscripcionId = inscripcionId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.fechaPago = fechaPago;
    }

    public Long getId() {
        return id;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
}