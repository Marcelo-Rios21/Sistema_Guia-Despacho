package com.duoc.LearningPlatformValidation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.repository.InscripcionRepository;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Inscripcion> listarInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion buscarInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + id));
    }

    public List<Inscripcion> buscarInscripcionesPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId);
    }

    public Inscripcion registrarInscripcion(Inscripcion inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion actualizarInscripcion(Long id, Inscripcion inscripcionActualizada) {
        Inscripcion inscripcionExistente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + id));

        inscripcionExistente.setCursoId(inscripcionActualizada.getCursoId());
        inscripcionExistente.setEstudianteId(inscripcionActualizada.getEstudianteId());
        inscripcionExistente.setFechaInscripcion(inscripcionActualizada.getFechaInscripcion());

        return inscripcionRepository.save(inscripcionExistente);
    }

    public void eliminarInscripcion(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Inscripción no encontrada con ID: " + id);
        }

        inscripcionRepository.deleteById(id);
    }
}