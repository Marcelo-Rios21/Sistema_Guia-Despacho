package com.duoc.LearningPlatformValidation.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "inscripcion_cursos")
public class InscripcionCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long inscripcionId;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private String nombreCurso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoCurso;

    public InscripcionCurso() {
    }

    public InscripcionCurso(Long id, Long inscripcionId, Long cursoId,
                            String nombreCurso, BigDecimal costoCurso) {
        this.id = id;
        this.inscripcionId = inscripcionId;
        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.costoCurso = costoCurso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public BigDecimal getCostoCurso() {
        return costoCurso;
    }

    public void setCostoCurso(BigDecimal costoCurso) {
        this.costoCurso = costoCurso;
    }
}
