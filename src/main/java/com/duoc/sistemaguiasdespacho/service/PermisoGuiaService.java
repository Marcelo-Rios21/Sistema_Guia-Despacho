package com.duoc.sistemaguiasdespacho.service;

import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import org.springframework.stereotype.Service;

@Service
public class PermisoGuiaService {

    private static final String ROL_ADMIN = "ADMIN";
    private static final String ROL_TRANSPORTISTA = "TRANSPORTISTA";

    public void validarAccesoAGuia(GuiaDespacho guia, String rolUsuario, String transportistaHeader) {
        if (guia == null) {
            throw new IllegalArgumentException("La guia no puede ser nula");
        }

        if (rolUsuario == null || rolUsuario.isBlank()) {
            throw new SecurityException("Debe indicar el rol del usuario en el header X-User-Role");
        }

        String rolNormalizado = rolUsuario.trim().toUpperCase();

        if (ROL_ADMIN.equals(rolNormalizado)) {
            return;
        }

        if (ROL_TRANSPORTISTA.equals(rolNormalizado)) {
            validarTransportistaPropietario(guia, transportistaHeader);
            return;
        }

        throw new SecurityException("Rol no autorizado para acceder a la guia");
    }

    private void validarTransportistaPropietario(GuiaDespacho guia, String transportistaHeader) {
        if (transportistaHeader == null || transportistaHeader.isBlank()) {
            throw new SecurityException("Debe indicar el transportista en el header X-Transportista");
        }

        String transportistaGuia = guia.getTransportista();

        if (transportistaGuia == null || !transportistaGuia.equalsIgnoreCase(transportistaHeader.trim())) {
            throw new SecurityException("No tiene permisos para acceder a esta guia");
        }
    }
}