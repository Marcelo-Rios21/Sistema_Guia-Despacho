package com.duoc.LearningPlatformValidation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BoletaResponse {

    private String numeroBoleta;
    private Long inscripcionId;
    private Long estudianteId;
    private LocalDate fechaEmision;
    private List<CursoInscritoResponse> cursos;
    private BigDecimal total;
    private String metodoPago;
    private String estadoPago;

    public BoletaResponse(String numeroBoleta, Long inscripcionId, Long estudianteId,
                          LocalDate fechaEmision, List<CursoInscritoResponse> cursos,
                          BigDecimal total, String metodoPago, String estadoPago) {
        this.numeroBoleta = numeroBoleta;
        this.inscripcionId = inscripcionId;
        this.estudianteId = estudianteId;
        this.fechaEmision = fechaEmision;
        this.cursos = cursos;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
    }

    public String getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
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

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
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
