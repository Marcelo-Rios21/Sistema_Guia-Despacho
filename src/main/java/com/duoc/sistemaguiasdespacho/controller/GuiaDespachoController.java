package com.duoc.sistemaguiasdespacho.controller;

import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoRequest;
import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoResponse;
import com.duoc.sistemaguiasdespacho.service.GuiaDespachoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
public class GuiaDespachoController {

    private final GuiaDespachoService guiaDespachoService;

    public GuiaDespachoController(GuiaDespachoService guiaDespachoService) {
        this.guiaDespachoService = guiaDespachoService;
    }

    @PostMapping
    public ResponseEntity<GuiaDespachoResponse> crearGuia(@RequestBody GuiaDespachoRequest request) {
        GuiaDespachoResponse response = guiaDespachoService.crearGuia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/s3")
    public ResponseEntity<GuiaDespachoResponse> subirGuiaAS3(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String rolUsuario,
            @RequestHeader(value = "X-Transportista", required = false) String transportista
    ) {
        GuiaDespachoResponse response = guiaDespachoService.subirGuiaAS3(id, rolUsuario, transportista);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuiaDespachoResponse> actualizarGuia(
            @PathVariable Long id,
            @RequestBody GuiaDespachoRequest request,
            @RequestHeader("X-User-Role") String rolUsuario,
            @RequestHeader(value = "X-Transportista", required = false) String transportista
    ) {
        GuiaDespachoResponse response = guiaDespachoService.actualizarGuia(id, request, rolUsuario, transportista);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GuiaDespachoResponse> eliminarGuiaDesdeS3(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String rolUsuario,
            @RequestHeader(value = "X-Transportista", required = false) String transportista
    ) {
        GuiaDespachoResponse response = guiaDespachoService.eliminarGuiaDesdeS3(id, rolUsuario, transportista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarGuiaDesdeS3(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String rolUsuario,
            @RequestHeader(value = "X-Transportista", required = false) String transportista
    ) {
        byte[] archivo = guiaDespachoService.descargarGuiaDesdeS3(id, rolUsuario, transportista);
        String nombreArchivo = guiaDespachoService.obtenerNombreArchivoDescarga(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(nombreArchivo)
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaDespachoResponse> buscarPorId(@PathVariable Long id) {
        GuiaDespachoResponse response = guiaDespachoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GuiaDespachoResponse>> consultarGuias(
            @RequestParam(required = false) String transportista,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        List<GuiaDespachoResponse> guias = guiaDespachoService.consultarPorTransportistaYFecha(transportista, fecha);
        return ResponseEntity.ok(guias);
    }
}