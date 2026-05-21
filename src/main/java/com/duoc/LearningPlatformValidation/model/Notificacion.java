package com.duoc.LearningPlatformValidation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private String tipo;
    private Boolean leida;
    private LocalDateTime fechaCreacion;

    public Notificacion() {
    }

    public Notificacion(Long id, Long usuarioId, String titulo, String mensaje, String tipo,
            Boolean leida, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leida = leida;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public Boolean getLeida() {
    return leida;
}

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    
}
