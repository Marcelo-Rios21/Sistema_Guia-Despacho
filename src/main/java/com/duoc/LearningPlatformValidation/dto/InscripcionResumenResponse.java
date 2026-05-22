package com.duoc.LearningPlatformValidation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InscripcionResumenResponse {

    private Long inscripcionId;
    private Long estudianteId;
    private LocalDate fechaInscripcion;
    private List<CursoInscritoResponse> cursos;
    private BigDecimal total;
    private String metodoPago;
    private String estadoPago;

    public InscripcionResumenResponse(Long inscripcionId, Long estudianteId,
                                      LocalDate fechaInscripcion,
                                      List<CursoInscritoResponse> cursos,
                                      BigDecimal total,
                                      String metodoPago,
                                      String estadoPago) {
        this.inscripcionId = inscripcionId;
        this.estudianteId = estudianteId;
        this.fechaInscripcion = fechaInscripcion;
        this.cursos = cursos;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public List<CursoInscritoResponse> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoInscritoResponse> cursos) {
        this.cursos = cursos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    
}
