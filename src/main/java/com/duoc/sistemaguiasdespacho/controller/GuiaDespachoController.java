package com.duoc.sistemaguiasdespacho.controller;

import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoRequest;
import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoResponse;
import com.duoc.sistemaguiasdespacho.service.GuiaDespachoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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