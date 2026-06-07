package com.duoc.sistemaguiasdespacho.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "GUIAS_DESPACHO")
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO_GUIA", nullable = false, unique = true, length = 50)
    private String numeroGuia;

    @Column(name = "TRANSPORTISTA", nullable = false, length = 100)
    private String transportista;

    @Column(name = "FECHA_EMISION", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "DESTINATARIO", nullable = false, length = 150)
    private String destinatario;

    @Column(name = "DIRECCION_DESTINO", nullable = false, length = 250)
    private String direccionDestino;

    @Column(name = "DESCRIPCION_CARGA", nullable = false, length = 1000)
    private String descripcionCarga;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 30)
    private EstadoGuia estado;

    @Column(name = "RUTA_EFS", length = 500)
    private String rutaEfs;

    @Column(name = "RUTA_S3", length = 500)
    private String rutaS3;

    @Column(name = "CREADO_POR", nullable = false, length = 100)
    private String creadoPor;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public GuiaDespacho() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.estado == null) {
            this.estado = EstadoGuia.GENERADA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getDescripcionCarga() {
        return descripcionCarga;
    }

    public void setDescripcionCarga(String descripcionCarga) {
        this.descripcionCarga = descripcionCarga;
    }

    public EstadoGuia getEstado() {
        return estado;
    }

    public void setEstado(EstadoGuia estado) {
        this.estado = estado;
    }

    public String getRutaEfs() {
        return rutaEfs;
    }

    public void setRutaEfs(String rutaEfs) {
        this.rutaEfs = rutaEfs;
    }

    public String getRutaS3() {
        return rutaS3;
    }

    public void setRutaS3(String rutaS3) {
        this.rutaS3 = rutaS3;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
