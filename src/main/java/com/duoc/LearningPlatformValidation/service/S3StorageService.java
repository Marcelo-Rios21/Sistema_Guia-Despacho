package com.duoc.LearningPlatformValidation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String construirRutaResumen(String numeroResumen, String nombreArchivo) {
        return numeroResumen + "/" + nombreArchivo;
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
}