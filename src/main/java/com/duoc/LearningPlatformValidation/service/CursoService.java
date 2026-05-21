package com.duoc.LearningPlatformValidation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningPlatformValidation.exception.RecursoNoEncontradoException;
import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarCursoPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + id));
    }

    public Curso registrarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso actualizarCurso(Long id, Curso cursoActualizado) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + id));

        cursoExistente.setNombre(cursoActualizado.getNombre());
        cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
        cursoExistente.setProfesorId(cursoActualizado.getProfesorId());

        return cursoRepository.save(cursoExistente);
    }

    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Curso no encontrado con ID: " + id);
        }

        cursoRepository.deleteById(id);
    }
}
