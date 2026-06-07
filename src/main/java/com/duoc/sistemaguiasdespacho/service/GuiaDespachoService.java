package com.duoc.sistemaguiasdespacho.service;

import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoRequest;
import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoResponse;
import com.duoc.sistemaguiasdespacho.exception.RecursoNoEncontradoException;
import com.duoc.sistemaguiasdespacho.model.EstadoGuia;
import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import com.duoc.sistemaguiasdespacho.repository.GuiaDespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class GuiaDespachoService {

    private final GuiaDespachoRepository guiaDespachoRepository;
    private final EfsStorageService efsStorageService;

    public GuiaDespachoService(
            GuiaDespachoRepository guiaDespachoRepository,
            EfsStorageService efsStorageService
    ) {
        this.guiaDespachoRepository = guiaDespachoRepository;
        this.efsStorageService = efsStorageService;
    }

    @Transactional
    public GuiaDespachoResponse crearGuia(GuiaDespachoRequest request) {
        validarRequestCreacion(request);

        if (guiaDespachoRepository.existsByNumeroGuia(request.getNumeroGuia())) {
            throw new IllegalArgumentException("Ya existe una guia con el numero: " + request.getNumeroGuia());
        }

        GuiaDespacho guia = new GuiaDespacho();
        guia.setNumeroGuia(request.getNumeroGuia().trim());
        guia.setTransportista(request.getTransportista().trim());
        guia.setFechaEmision(request.getFechaEmision());
        guia.setDestinatario(request.getDestinatario().trim());
        guia.setDireccionDestino(request.getDireccionDestino().trim());
        guia.setDescripcionCarga(request.getDescripcionCarga().trim());
        guia.setCreadoPor(request.getCreadoPor().trim());
        guia.setEstado(EstadoGuia.GENERADA);

        GuiaDespacho guiaGuardada = guiaDespachoRepository.save(guia);

        Path rutaPdf = efsStorageService.generarGuiaPdf(guiaGuardada);
        guiaGuardada.setRutaEfs(rutaPdf.toString());

        GuiaDespacho guiaActualizada = guiaDespachoRepository.save(guiaGuardada);

        return convertirAResponse(guiaActualizada);
    }

    @Transactional(readOnly = true)
    public GuiaDespachoResponse buscarPorId(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);
        return convertirAResponse(guia);
    }

    @Transactional(readOnly = true)
    public List<GuiaDespachoResponse> listarGuias() {
        return guiaDespachoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuiaDespachoResponse> consultarPorTransportistaYFecha(String transportista, LocalDate fecha) {
        List<GuiaDespacho> guias;

        boolean tieneTransportista = transportista != null && !transportista.isBlank();
        boolean tieneFecha = fecha != null;

        if (tieneTransportista && tieneFecha) {
            guias = guiaDespachoRepository.findByTransportistaIgnoreCaseAndFechaEmision(transportista.trim(), fecha);
        } else if (tieneTransportista) {
            guias = guiaDespachoRepository.findByTransportistaIgnoreCase(transportista.trim());
        } else if (tieneFecha) {
            guias = guiaDespachoRepository.findByFechaEmision(fecha);
        } else {
            guias = guiaDespachoRepository.findAll();
        }

        return guias.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private GuiaDespacho obtenerEntidadPorId(Long id) {
        return guiaDespachoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la guia con id: " + id));
    }

    private void validarRequestCreacion(GuiaDespachoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede estar vacia");
        }

        validarTextoObligatorio(request.getNumeroGuia(), "numeroGuia");
        validarTextoObligatorio(request.getTransportista(), "transportista");
        validarTextoObligatorio(request.getDestinatario(), "destinatario");
        validarTextoObligatorio(request.getDireccionDestino(), "direccionDestino");
        validarTextoObligatorio(request.getDescripcionCarga(), "descripcionCarga");
        validarTextoObligatorio(request.getCreadoPor(), "creadoPor");

        if (request.getFechaEmision() == null) {
            throw new IllegalArgumentException("El campo fechaEmision es obligatorio");
        }
    }

    private void validarTextoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }

    private GuiaDespachoResponse convertirAResponse(GuiaDespacho guia) {
        GuiaDespachoResponse response = new GuiaDespachoResponse();
        response.setId(guia.getId());
        response.setNumeroGuia(guia.getNumeroGuia());
        response.setTransportista(guia.getTransportista());
        response.setFechaEmision(guia.getFechaEmision());
        response.setDestinatario(guia.getDestinatario());
        response.setDireccionDestino(guia.getDireccionDestino());
        response.setDescripcionCarga(guia.getDescripcionCarga());
        response.setEstado(guia.getEstado());
        response.setRutaEfs(guia.getRutaEfs());
        response.setRutaS3(guia.getRutaS3());
        response.setCreadoPor(guia.getCreadoPor());
        response.setCreatedAt(guia.getCreatedAt());
        response.setUpdatedAt(guia.getUpdatedAt());
        return response;
    }
}