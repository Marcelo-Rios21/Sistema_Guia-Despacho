package com.duoc.sistemaguiasdespacho.service;

import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Service
public class EfsStorageService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Value("${app.storage.efs-path}")
    private String efsBasePath;

    public Path generarGuiaPdf(GuiaDespacho guia) {
        try {
            Path rutaArchivo = construirRutaArchivo(guia);
            Files.createDirectories(rutaArchivo.getParent());

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 740);

                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.showText("Guia de Despacho");
                    contentStream.newLineAtOffset(0, -35);

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    escribirLinea(contentStream, "Numero de guia: " + guia.getNumeroGuia());
                    escribirLinea(contentStream, "Transportista: " + guia.getTransportista());
                    escribirLinea(contentStream, "Fecha de emision: " + guia.getFechaEmision());
                    escribirLinea(contentStream, "Destinatario: " + guia.getDestinatario());
                    escribirLinea(contentStream, "Direccion destino: " + guia.getDireccionDestino());
                    escribirLinea(contentStream, "Creado por: " + guia.getCreadoPor());
                    escribirLinea(contentStream, "Estado: " + guia.getEstado());

                    contentStream.newLineAtOffset(0, -15);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    escribirLinea(contentStream, "Descripcion de carga:");

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    escribirLinea(contentStream, limpiarTexto(guia.getDescripcionCarga()));

                    contentStream.endText();
                }

                document.save(rutaArchivo.toFile());
            }

            return rutaArchivo;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo generar la guia en almacenamiento temporal EFS", e);
        }
    }

    public Path construirRutaArchivo(GuiaDespacho guia) {
        String fecha = guia.getFechaEmision().format(DATE_FORMATTER);
        String transportistaNormalizado = normalizarSegmentoRuta(guia.getTransportista());
        String numeroNormalizado = normalizarSegmentoRuta(guia.getNumeroGuia());

        return Path.of(
                efsBasePath,
                fecha,
                transportistaNormalizado,
                "guia-" + numeroNormalizado + ".pdf"
        );
    }

    private void escribirLinea(PDPageContentStream contentStream, String texto) throws IOException {
        contentStream.showText(limpiarTexto(texto));
        contentStream.newLineAtOffset(0, -20);
    }

    private String normalizarSegmentoRuta(String valor) {
        if (valor == null || valor.isBlank()) {
            return "sin-dato";
        }

        return valor.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("[^\\p{ASCII}]", "");
    }
}