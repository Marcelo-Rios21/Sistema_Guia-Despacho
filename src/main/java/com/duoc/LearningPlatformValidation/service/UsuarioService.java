package com.duoc.LearningPlatformValidation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Usuario;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
        usuarioExistente.setRol(usuarioActualizado.getRol());

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }

        usuarioRepository.deleteById(id);
    }
}