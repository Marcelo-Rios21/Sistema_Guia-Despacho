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

import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.service.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public ResponseEntity<List<Inscripcion>> listarInscripciones() {
        return ResponseEntity.ok(inscripcionService.listarInscripciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> buscarInscripcionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.buscarInscripcionPorId(id));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> buscarInscripcionesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.buscarInscripcionesPorCurso(cursoId));
    }

    @PostMapping
    public ResponseEntity<Inscripcion> registrarInscripcion(@RequestBody Inscripcion inscripcion) {
        Inscripcion nuevaInscripcion = inscripcionService.registrarInscripcion(inscripcion);
        URI location = URI.create("/api/inscripciones/" + nuevaInscripcion.getId());

        return ResponseEntity.created(location).body(nuevaInscripcion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> actualizarInscripcion(
            @PathVariable Long id,
            @RequestBody Inscripcion inscripcion) {

        Inscripcion inscripcionActualizada = inscripcionService.actualizarInscripcion(id, inscripcion);
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}