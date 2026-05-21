package com.duoc.LearningPlatformValidation.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.service.EvaluacionService;
@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @GetMapping
    public ResponseEntity<List<Evaluacion>> listarEvaluaciones() {
        return ResponseEntity.ok(evaluacionService.listarEvaluaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> buscarEvaluacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.buscarEvaluacionPorId(id));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Evaluacion>> buscarEvaluacionesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(evaluacionService.buscarEvaluacionesPorCurso(cursoId));
    }

    @PostMapping
    public ResponseEntity<Evaluacion> registrarEvaluacion(@RequestBody Evaluacion evaluacion) {
        Evaluacion nuevaEvaluacion = evaluacionService.registrarEvaluacion(evaluacion);
        URI location = URI.create("/api/evaluaciones/" + nuevaEvaluacion.getId());

        return ResponseEntity.created(location).body(nuevaEvaluacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizarEvaluacion(
            @PathVariable Long id,
            @RequestBody Evaluacion evaluacion) {

        Evaluacion evaluacionActualizada = evaluacionService.actualizarEvaluacion(id, evaluacion);
        return ResponseEntity.ok(evaluacionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        evaluacionService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
}
