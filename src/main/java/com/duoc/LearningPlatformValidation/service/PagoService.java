package com.duoc.LearningPlatformValidation.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Pago;
import com.duoc.LearningPlatformValidation.repository.PagoRepository;

@Service
public class PagoService {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_RECHAZADO = "RECHAZADO";
    public static final String ESTADO_APROBADO_SIMULADO = "APROBADO_SIMULADO";

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            ESTADO_PENDIENTE,
            ESTADO_APROBADO,
            ESTADO_RECHAZADO,
            ESTADO_APROBADO_SIMULADO
    );

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Pago buscarPagoPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con ID: " + id));
    }

    public List<Pago> buscarPagosPorInscripcion(Long inscripcionId) {
        return pagoRepository.findByInscripcionId(inscripcionId);
    }

    public List<Pago> buscarPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    public Pago registrarPago(Pago pago) {
        validarPago(pago);

        if (pago.getEstado() == null || pago.getEstado().isBlank()) {
            pago.setEstado(ESTADO_PENDIENTE);
        }

        validarEstado(pago.getEstado());

        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }

        return pagoRepository.save(pago);
    }

    public Pago actualizarPago(Long id, Pago pagoActualizado) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con ID: " + id));

        validarPago(pagoActualizado);
        validarEstado(pagoActualizado.getEstado());

        pagoExistente.setInscripcionId(pagoActualizado.getInscripcionId());
        pagoExistente.setMonto(pagoActualizado.getMonto());
        pagoExistente.setMetodoPago(pagoActualizado.getMetodoPago());
        pagoExistente.setEstado(pagoActualizado.getEstado());
        pagoExistente.setFechaPago(
                pagoActualizado.getFechaPago() != null
                        ? pagoActualizado.getFechaPago()
                        : LocalDateTime.now()
        );

        return pagoRepository.save(pagoExistente);
    }

    public Pago aprobarPago(Long id) {
        Pago pago = buscarPagoPorId(id);
        pago.setEstado(ESTADO_APROBADO);
        pago.setFechaPago(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public Pago rechazarPago(Long id) {
        Pago pago = buscarPagoPorId(id);
        pago.setEstado(ESTADO_RECHAZADO);
        pago.setFechaPago(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pago no encontrado con ID: " + id);
        }

        pagoRepository.deleteById(id);
    }

    private void validarPago(Pago pago) {
        if (pago.getInscripcionId() == null) {
            throw new IllegalArgumentException("La inscripción asociada al pago es obligatoria.");
        }

        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }

        if (pago.getMetodoPago() == null || pago.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("El método de pago es obligatorio.");
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado del pago es obligatorio.");
        }

        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException("Estado de pago no válido: " + estado);
        }
    }
}