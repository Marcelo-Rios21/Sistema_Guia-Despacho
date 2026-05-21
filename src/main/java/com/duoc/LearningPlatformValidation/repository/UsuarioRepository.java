package com.duoc.LearningPlatformValidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.LearningPlatformValidation.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
