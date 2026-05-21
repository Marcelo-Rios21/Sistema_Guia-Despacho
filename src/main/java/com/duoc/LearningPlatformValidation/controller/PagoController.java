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

import com.duoc.LearningPlatformValidation.model.Pago;
import com.duoc.LearningPlatformValidation.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPagoPorId(id));
    }

    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<Pago>> buscarPagosPorInscripcion(@PathVariable Long inscripcionId) {
        return ResponseEntity.ok(pagoService.buscarPagosPorInscripcion(inscripcionId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> buscarPagosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.buscarPagosPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Pago> registrarPago(@RequestBody Pago pago) {
        Pago nuevoPago = pagoService.registrarPago(pago);
        URI location = URI.create("/api/pagos/" + nuevoPago.getId());

        return ResponseEntity.created(location).body(nuevoPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(
            @PathVariable Long id,
            @RequestBody Pago pago) {

        Pago pagoActualizado = pagoService.actualizarPago(id, pago);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Pago> aprobarPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.aprobarPago(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazarPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.rechazarPago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}