package com.duoc.LearningPlatformValidation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.repository.EvaluacionRepository;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    public List<Evaluacion> listarEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    public Evaluacion buscarEvaluacionPorId(Long id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada con ID: " + id));
    }

    public List<Evaluacion> buscarEvaluacionesPorCurso(Long cursoId) {
        return evaluacionRepository.findByCursoId(cursoId);
    }

    public Evaluacion registrarEvaluacion(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion actualizarEvaluacion(Long id, Evaluacion evaluacionActualizada) {
        Evaluacion evaluacionExistente = evaluacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada con ID: " + id));

        evaluacionExistente.setCursoId(evaluacionActualizada.getCursoId());
        evaluacionExistente.setNombre(evaluacionActualizada.getNombre());
        evaluacionExistente.setPuntajeMaximo(evaluacionActualizada.getPuntajeMaximo());
        evaluacionExistente.setFechaAplicacion(evaluacionActualizada.getFechaAplicacion());

        return evaluacionRepository.save(evaluacionExistente);
    }

    public void eliminarEvaluacion(Long id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Evaluación no encontrada con ID: " + id);
        }

        evaluacionRepository.deleteById(id);
    }
}