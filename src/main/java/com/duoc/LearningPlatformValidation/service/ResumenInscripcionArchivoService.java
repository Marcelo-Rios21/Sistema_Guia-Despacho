package com.duoc.LearningPlatformValidation.service;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.dto.BoletaResponse;
import com.duoc.LearningPlatformValidation.dto.CursoInscritoResponse;


@Service
public class ResumenInscripcionArchivoService {

    public byte[] generarArchivoResumen(BoletaResponse boleta) {
        StringBuilder contenido = new StringBuilder();

        contenido.append("RESUMEN DE INSCRIPCION\n");
        contenido.append("=======================\n\n");

        contenido.append("Numero de resumen: ").append(boleta.getNumeroBoleta()).append("\n");
        contenido.append("ID de inscripcion: ").append(boleta.getInscripcionId()).append("\n");
        contenido.append("ID estudiante: ").append(boleta.getEstudianteId()).append("\n");
        contenido.append("Fecha de emision: ").append(boleta.getFechaEmision()).append("\n\n");

        contenido.append("Cursos inscritos:\n");

        if (boleta.getCursos() != null && !boleta.getCursos().isEmpty()) {
            for (CursoInscritoResponse curso : boleta.getCursos()) {
                contenido.append("- ")
                        .append(curso.getNombre())
                        .append(" | ID curso: ")
                        .append(curso.getCursoId())
                        .append(" | Costo: $")
                        .append(curso.getCosto())
                        .append("\n");
            }
        } else {
            contenido.append("- No hay cursos asociados.\n");
        }

        contenido.append("\n");
        contenido.append("Total pagado: $").append(boleta.getTotal()).append("\n");
        contenido.append("Metodo de pago: ").append(boleta.getMetodoPago()).append("\n");
        contenido.append("Estado de pago: ").append(boleta.getEstadoPago()).append("\n");

        return contenido.toString().getBytes(StandardCharsets.UTF_8);
    }

    public String generarNombreArchivo(BoletaResponse boleta) {
        return "resumen-inscripcion-" + boleta.getNumeroBoleta() + ".txt";
    }
}
