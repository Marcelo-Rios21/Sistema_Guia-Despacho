package com.duoc.LearningPlatformValidation.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.LearningPlatformValidation.dto.BoletaResponse;
import com.duoc.LearningPlatformValidation.dto.InscripcionRequest;
import com.duoc.LearningPlatformValidation.dto.InscripcionResumenResponse;
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

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Inscripcion>> buscarInscripcionesPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(inscripcionService.buscarInscripcionesPorEstudiante(estudianteId));
    }

    @PostMapping
    public ResponseEntity<InscripcionResumenResponse> registrarInscripcion(@RequestBody InscripcionRequest request) {
        InscripcionResumenResponse resumen = inscripcionService.registrarInscripcion(request);
        URI location = URI.create("/api/inscripciones/" + resumen.getInscripcionId());
        return ResponseEntity.created(location).body(resumen);
    }

    @GetMapping("/{id}/boleta")
    public ResponseEntity<BoletaResponse> generarBoleta(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.generarBoleta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}