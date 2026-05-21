package com.duoc.LearningPlatformValidation.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cursoId;
    private String nombre;
    private int puntajeMaximo;
    private LocalDate fechaAplicacion;

    public Evaluacion() {
    }

    public Evaluacion(Long id, Long cursoId, String nombre, int puntajeMaximo, LocalDate fechaAplicacion) {
        this.id = id;
        this.cursoId = cursoId;
        this.nombre = nombre;
        this.puntajeMaximo = puntajeMaximo;
        this.fechaAplicacion = fechaAplicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    
}
