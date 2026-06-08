package com.duoc.sistemaguiasdespacho.service;

import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Service
public class S3StorageService {

    private static final String CONTENT_TYPE_PDF = "application/pdf";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String construirRutaGuia(GuiaDespacho guia) {
        String fecha = guia.getFechaEmision().format(DATE_FORMATTER);
        String transportistaNormalizado = normalizarSegmentoRuta(guia.getTransportista());
        String numeroNormalizado = normalizarSegmentoRuta(guia.getNumeroGuia());

        return "guias/"
                + fecha + "/"
                + transportistaNormalizado + "/"
                + "guia-" + numeroNormalizado + ".pdf";
    }

    public String subirGuiaDesdeRuta(GuiaDespacho guia, Path rutaArchivoLocal) {
        try {
            byte[] contenido = Files.readAllBytes(rutaArchivoLocal);
            String rutaS3 = construirRutaGuia(guia);
            return subirArchivo(rutaS3, contenido, CONTENT_TYPE_PDF);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la guia generada para subirla a S3", e);
        }
    }

    public String subirArchivo(String rutaArchivo, byte[] contenido, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(rutaArchivo)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(contenido));

        return rutaArchivo;
    }

    public byte[] descargarArchivo(String rutaArchivo) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(rutaArchivo)
                .build();

        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);

        return response.asByteArray();
    }

    public String actualizarGuiaDesdeRuta(GuiaDespacho guia, Path rutaArchivoLocal) {
        return subirGuiaDesdeRuta(guia, rutaArchivoLocal);
    }

    public String modificarArchivo(String rutaArchivo, byte[] nuevoContenido, String contentType) {
        return subirArchivo(rutaArchivo, nuevoContenido, contentType);
    }

    public void eliminarArchivo(String rutaArchivo) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(rutaArchivo)
                .build();

        s3Client.deleteObject(request);
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
}