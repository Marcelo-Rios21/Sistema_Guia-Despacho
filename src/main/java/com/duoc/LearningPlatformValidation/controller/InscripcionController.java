package com.duoc.LearningPlatformValidation.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.LearningPlatformValidation.dto.BoletaResponse;
import com.duoc.LearningPlatformValidation.dto.InscripcionRequest;
import com.duoc.LearningPlatformValidation.dto.InscripcionResumenResponse;
import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.service.InscripcionService;
import com.duoc.LearningPlatformValidation.service.ResumenInscripcionArchivoService;
import com.duoc.LearningPlatformValidation.service.S3StorageService;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final ResumenInscripcionArchivoService resumenInscripcionArchivoService;
    private final S3StorageService s3StorageService;

    public InscripcionController(
            InscripcionService inscripcionService,
            ResumenInscripcionArchivoService resumenInscripcionArchivoService,
            S3StorageService s3StorageService
    ) {
        this.inscripcionService = inscripcionService;
        this.resumenInscripcionArchivoService = resumenInscripcionArchivoService;
        this.s3StorageService = s3StorageService;
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

    @GetMapping("/{id}/resumen/s3/download")
    public ResponseEntity<ByteArrayResource> descargarResumenDesdeS3(@PathVariable Long id) {
        BoletaResponse boleta = inscripcionService.generarBoleta(id);

        String nombreArchivo = resumenInscripcionArchivoService.generarNombreArchivo(boleta);

        String rutaArchivo = s3StorageService.construirRutaResumen(
                boleta.getNumeroBoleta(),
                nombreArchivo
        );

        byte[] archivo = s3StorageService.descargarArchivo(rutaArchivo);
        ByteArrayResource recurso = new ByteArrayResource(archivo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(archivo.length)
                .body(recurso);
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

    @GetMapping("/{id}/resumen/archivo")
    public ResponseEntity<ByteArrayResource> descargarResumenComoArchivo(@PathVariable Long id) {
        BoletaResponse boleta = inscripcionService.generarBoleta(id);

        byte[] archivo = resumenInscripcionArchivoService.generarArchivoResumen(boleta);
        String nombreArchivo = resumenInscripcionArchivoService.generarNombreArchivo(boleta);

        ByteArrayResource recurso = new ByteArrayResource(archivo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(archivo.length)
                .body(recurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/resumen/s3")
    public ResponseEntity<Map<String, String>> eliminarResumenDeS3(@PathVariable Long id) {
        BoletaResponse boleta = inscripcionService.generarBoleta(id);

        String nombreArchivo = resumenInscripcionArchivoService.generarNombreArchivo(boleta);

        String rutaArchivo = s3StorageService.construirRutaResumen(
                boleta.getNumeroBoleta(),
                nombreArchivo
        );

        s3StorageService.eliminarArchivo(rutaArchivo);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Resumen de inscripción eliminado correctamente de S3",
                "numeroResumen", boleta.getNumeroBoleta(),
                "archivo", nombreArchivo,
                "rutaS3", rutaArchivo
        ));
    }

    @PostMapping("/{id}/resumen/s3")
    public ResponseEntity<Map<String, String>> subirResumenAS3(@PathVariable Long id) {
        BoletaResponse boleta = inscripcionService.generarBoleta(id);

        byte[] archivo = resumenInscripcionArchivoService.generarArchivoResumen(boleta);
        String nombreArchivo = resumenInscripcionArchivoService.generarNombreArchivo(boleta);

        String rutaArchivo = s3StorageService.construirRutaResumen(
                boleta.getNumeroBoleta(),
                nombreArchivo
        );

        s3StorageService.subirArchivo(
                rutaArchivo,
                archivo,
                MediaType.TEXT_PLAIN_VALUE
        );

        return ResponseEntity.ok(Map.of(
                "mensaje", "Resumen de inscripción subido correctamente a S3",
                "numeroResumen", boleta.getNumeroBoleta(),
                "archivo", nombreArchivo,
                "rutaS3", rutaArchivo
        ));
    }

    @PutMapping("/{id}/resumen/s3")
    public ResponseEntity<Map<String, String>> modificarResumenEnS3(
            @PathVariable Long id,
            @RequestBody String nuevoContenido
    ) {
        BoletaResponse boleta = inscripcionService.generarBoleta(id);

        String nombreArchivo = resumenInscripcionArchivoService.generarNombreArchivo(boleta);

        String rutaArchivo = s3StorageService.construirRutaResumen(
                boleta.getNumeroBoleta(),
                nombreArchivo
        );

        s3StorageService.modificarArchivo(
                rutaArchivo,
                nuevoContenido.getBytes(StandardCharsets.UTF_8),
                MediaType.TEXT_PLAIN_VALUE
        );

        return ResponseEntity.ok(Map.of(
                "mensaje", "Resumen de inscripción modificado correctamente en S3",
                "numeroResumen", boleta.getNumeroBoleta(),
                "archivo", nombreArchivo,
                "rutaS3", rutaArchivo
        ));
    }
}