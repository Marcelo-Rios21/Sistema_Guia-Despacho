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

import com.duoc.LearningPlatformValidation.model.Notificacion;
import com.duoc.LearningPlatformValidation.service.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        return ResponseEntity.ok(notificacionService.listarNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarNotificacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.buscarNotificacionPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> buscarNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.buscarNotificacionesPorUsuario(usuarioId));
    }

    @GetMapping("/leida/{leida}")
    public ResponseEntity<List<Notificacion>> buscarNotificacionesPorEstado(@PathVariable boolean leida) {
        return ResponseEntity.ok(notificacionService.buscarNotificacionesPorEstado(leida));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> buscarNotificacionesPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(notificacionService.buscarNotificacionesPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<Notificacion> registrarNotificacion(@RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionService.registrarNotificacion(notificacion);
        URI location = URI.create("/api/notificaciones/" + nuevaNotificacion.getId());

        return ResponseEntity.created(location).body(nuevaNotificacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizarNotificacion(
            @PathVariable Long id,
            @RequestBody Notificacion notificacion) {

        Notificacion notificacionActualizada = notificacionService.actualizarNotificacion(id, notificacion);
        return ResponseEntity.ok(notificacionActualizada);
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
