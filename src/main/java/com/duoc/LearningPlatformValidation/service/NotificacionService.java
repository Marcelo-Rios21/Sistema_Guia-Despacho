package com.duoc.LearningPlatformValidation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Notificacion;
import com.duoc.LearningPlatformValidation.repository.NotificacionRepository;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> listarNotificaciones() {
        return notificacionRepository.findAll();
    }

    public Notificacion buscarNotificacionPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada con ID: " + id));
    }

    public List<Notificacion> buscarNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> buscarNotificacionesPorEstado(boolean leida) {
        return notificacionRepository.findByLeida(leida);
    }

    public List<Notificacion> buscarNotificacionesPorTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo);
    }

    public Notificacion registrarNotificacion(Notificacion notificacion) {
        validarNotificacion(notificacion);

        notificacion.setLeida(false);

        if (notificacion.getFechaCreacion() == null) {
            notificacion.setFechaCreacion(LocalDateTime.now());
        }

        return notificacionRepository.save(notificacion);
    }

    public Notificacion actualizarNotificacion(Long id, Notificacion notificacionActualizada) {
        Notificacion notificacionExistente = notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada con ID: " + id));

        validarNotificacion(notificacionActualizada);

        notificacionExistente.setUsuarioId(notificacionActualizada.getUsuarioId());
        notificacionExistente.setTitulo(notificacionActualizada.getTitulo());
        notificacionExistente.setMensaje(notificacionActualizada.getMensaje());
        notificacionExistente.setTipo(notificacionActualizada.getTipo());
        notificacionExistente.setLeida(notificacionActualizada.getLeida());
        notificacionExistente.setFechaCreacion(notificacionActualizada.getFechaCreacion());

        return notificacionRepository.save(notificacionExistente);
    }

    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = buscarNotificacionPorId(id);
        notificacion.setLeida(true);

        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Notificación no encontrada con ID: " + id);
        }

        notificacionRepository.deleteById(id);
    }

    private void validarNotificacion(Notificacion notificacion) {
        if (notificacion.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario asociado a la notificación es obligatorio.");
        }

        if (notificacion.getTitulo() == null || notificacion.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título de la notificación es obligatorio.");
        }

        if (notificacion.getMensaje() == null || notificacion.getMensaje().isBlank()) {
            throw new IllegalArgumentException("El mensaje de la notificación es obligatorio.");
        }

        if (notificacion.getTipo() == null || notificacion.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de notificación es obligatorio.");
        }
    }
}
