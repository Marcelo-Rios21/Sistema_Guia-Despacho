package com.duoc.sistemaguiasdespacho.service;

import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoRequest;
import com.duoc.sistemaguiasdespacho.dto.GuiaDespachoResponse;
import com.duoc.sistemaguiasdespacho.exception.RecursoNoEncontradoException;
import com.duoc.sistemaguiasdespacho.model.EstadoGuia;
import com.duoc.sistemaguiasdespacho.model.GuiaDespacho;
import com.duoc.sistemaguiasdespacho.repository.GuiaDespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class GuiaDespachoService {

    private final GuiaDespachoRepository guiaDespachoRepository;
    private final EfsStorageService efsStorageService;
    private final S3StorageService s3StorageService;

    public GuiaDespachoService(
            GuiaDespachoRepository guiaDespachoRepository,
            EfsStorageService efsStorageService,
            S3StorageService s3StorageService
    ) {
        this.guiaDespachoRepository = guiaDespachoRepository;
        this.efsStorageService = efsStorageService;
        this.s3StorageService = s3StorageService;
    }

    @Transactional
    public GuiaDespachoResponse crearGuia(GuiaDespachoRequest request) {
        validarRequestCreacion(request);

        if (guiaDespachoRepository.existsByNumeroGuia(request.getNumeroGuia())) {
            throw new IllegalArgumentException("Ya existe una guia con el numero: " + request.getNumeroGuia());
        }

        GuiaDespacho guia = new GuiaDespacho();
        aplicarDatosRequest(guia, request);
        guia.setEstado(EstadoGuia.GENERADA);

        GuiaDespacho guiaGuardada = guiaDespachoRepository.save(guia);

        Path rutaPdf = efsStorageService.generarGuiaPdf(guiaGuardada);
        guiaGuardada.setRutaEfs(rutaPdf.toString());

        GuiaDespacho guiaActualizada = guiaDespachoRepository.save(guiaGuardada);

        return convertirAResponse(guiaActualizada);
    }

    @Transactional
    public GuiaDespachoResponse subirGuiaAS3(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);

        if (guia.getRutaEfs() == null || guia.getRutaEfs().isBlank()) {
            throw new IllegalStateException("La guia no tiene una ruta EFS asociada");
        }

        Path rutaArchivoLocal = Path.of(guia.getRutaEfs());

        if (!Files.exists(rutaArchivoLocal)) {
            throw new IllegalStateException("No existe el archivo temporal de la guia en EFS: " + guia.getRutaEfs());
        }

        String rutaS3 = s3StorageService.subirGuiaDesdeRuta(guia, rutaArchivoLocal);

        guia.setRutaS3(rutaS3);
        guia.setEstado(EstadoGuia.SUBIDA_S3);

        GuiaDespacho guiaActualizada = guiaDespachoRepository.save(guia);

        return convertirAResponse(guiaActualizada);
    }

    @Transactional
    public GuiaDespachoResponse actualizarGuia(Long id, GuiaDespachoRequest request) {
        validarRequestCreacion(request);

        GuiaDespacho guia = obtenerEntidadPorId(id);

        String nuevoNumeroGuia = request.getNumeroGuia().trim();
        boolean cambiaNumeroGuia = !guia.getNumeroGuia().equalsIgnoreCase(nuevoNumeroGuia);

        if (cambiaNumeroGuia && guiaDespachoRepository.existsByNumeroGuia(nuevoNumeroGuia)) {
            throw new IllegalArgumentException("Ya existe una guia con el numero: " + nuevoNumeroGuia);
        }

        String rutaS3Anterior = guia.getRutaS3();

        aplicarDatosRequest(guia, request);
        guia.setEstado(EstadoGuia.ACTUALIZADA);

        GuiaDespacho guiaGuardada = guiaDespachoRepository.save(guia);

        Path rutaPdfActualizada = efsStorageService.generarGuiaPdf(guiaGuardada);
        guiaGuardada.setRutaEfs(rutaPdfActualizada.toString());

        String nuevaRutaS3 = s3StorageService.actualizarGuiaDesdeRuta(guiaGuardada, rutaPdfActualizada);
        guiaGuardada.setRutaS3(nuevaRutaS3);

        if (rutaS3Anterior != null && !rutaS3Anterior.isBlank() && !rutaS3Anterior.equals(nuevaRutaS3)) {
            s3StorageService.eliminarArchivo(rutaS3Anterior);
        }

        GuiaDespacho guiaActualizada = guiaDespachoRepository.save(guiaGuardada);

        return convertirAResponse(guiaActualizada);
    }

    @Transactional
    public GuiaDespachoResponse eliminarGuiaDesdeS3(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);

        if (guia.getRutaS3() == null || guia.getRutaS3().isBlank()) {
            throw new IllegalStateException("La guia no tiene archivo asociado en S3");
        }

        s3StorageService.eliminarArchivo(guia.getRutaS3());

        guia.setRutaS3(null);
        guia.setEstado(EstadoGuia.ELIMINADA);

        GuiaDespacho guiaActualizada = guiaDespachoRepository.save(guia);

        return convertirAResponse(guiaActualizada);
    }

    @Transactional(readOnly = true)
    public byte[] descargarGuiaDesdeS3(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);

        if (guia.getRutaS3() == null || guia.getRutaS3().isBlank()) {
            throw new IllegalStateException("La guia no ha sido subida a S3 o fue eliminada");
        }

        return s3StorageService.descargarArchivo(guia.getRutaS3());
    }

    @Transactional(readOnly = true)
    public String obtenerNombreArchivoDescarga(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);
        String numeroNormalizado = guia.getNumeroGuia()
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return "guia-" + numeroNormalizado + ".pdf";
    }

    @Transactional(readOnly = true)
    public GuiaDespachoResponse buscarPorId(Long id) {
        GuiaDespacho guia = obtenerEntidadPorId(id);
        return convertirAResponse(guia);
    }

    @Transactional(readOnly = true)
    public List<GuiaDespachoResponse> listarGuias() {
        return guiaDespachoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuiaDespachoResponse> consultarPorTransportistaYFecha(String transportista, LocalDate fecha) {
        List<GuiaDespacho> guias;

        boolean tieneTransportista = transportista != null && !transportista.isBlank();
        boolean tieneFecha = fecha != null;

        if (tieneTransportista && tieneFecha) {
            guias = guiaDespachoRepository.findByTransportistaIgnoreCaseAndFechaEmision(transportista.trim(), fecha);
        } else if (tieneTransportista) {
            guias = guiaDespachoRepository.findByTransportistaIgnoreCase(transportista.trim());
        } else if (tieneFecha) {
            guias = guiaDespachoRepository.findByFechaEmision(fecha);
        } else {
            guias = guiaDespachoRepository.findAll();
        }

        return guias.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private GuiaDespacho obtenerEntidadPorId(Long id) {
        return guiaDespachoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la guia con id: " + id));
    }

    private void aplicarDatosRequest(GuiaDespacho guia, GuiaDespachoRequest request) {
        guia.setNumeroGuia(request.getNumeroGuia().trim());
        guia.setTransportista(request.getTransportista().trim());
        guia.setFechaEmision(request.getFechaEmision());
        guia.setDestinatario(request.getDestinatario().trim());
        guia.setDireccionDestino(request.getDireccionDestino().trim());
        guia.setDescripcionCarga(request.getDescripcionCarga().trim());
        guia.setCreadoPor(request.getCreadoPor().trim());
    }

    private void validarRequestCreacion(GuiaDespachoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede estar vacia");
        }

        validarTextoObligatorio(request.getNumeroGuia(), "numeroGuia");
        validarTextoObligatorio(request.getTransportista(), "transportista");
        validarTextoObligatorio(request.getDestinatario(), "destinatario");
        validarTextoObligatorio(request.getDireccionDestino(), "direccionDestino");
        validarTextoObligatorio(request.getDescripcionCarga(), "descripcionCarga");
        validarTextoObligatorio(request.getCreadoPor(), "creadoPor");

        if (request.getFechaEmision() == null) {
            throw new IllegalArgumentException("El campo fechaEmision es obligatorio");
        }
    }

    private void validarTextoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }

    private GuiaDespachoResponse convertirAResponse(GuiaDespacho guia) {
        GuiaDespachoResponse response = new GuiaDespachoResponse();
        response.setId(guia.getId());
        response.setNumeroGuia(guia.getNumeroGuia());
        response.setTransportista(guia.getTransportista());
        response.setFechaEmision(guia.getFechaEmision());
        response.setDestinatario(guia.getDestinatario());
        response.setDireccionDestino(guia.getDireccionDestino());
        response.setDescripcionCarga(guia.getDescripcionCarga());
        response.setEstado(guia.getEstado());
        response.setRutaEfs(guia.getRutaEfs());
        response.setRutaS3(guia.getRutaS3());
        response.setCreadoPor(guia.getCreadoPor());
        response.setCreatedAt(guia.getCreatedAt());
        response.setUpdatedAt(guia.getUpdatedAt());
        return response;
    }
}