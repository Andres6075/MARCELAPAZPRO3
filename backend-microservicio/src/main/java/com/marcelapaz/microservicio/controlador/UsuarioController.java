package com.marcelapaz.microservicio.controlador;

import com.marcelapaz.microservicio.modelo.UsuarioEntity;
import com.marcelapaz.microservicio.repositorio.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        sembrarUsuarios();
    }

    private void sembrarUsuarios() {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new UsuarioEntity(null, "Estudiante Demo", "estudiante@escuelamarcelapaz.cl", "ESTUDIANTE"));
            usuarioRepository.save(new UsuarioEntity(null, "Docente Demo", "docente@escuelamarcelapaz.cl", "DOCENTE"));
            usuarioRepository.save(new UsuarioEntity(null, "UTP Demo", "utp@escuelamarcelapaz.cl", "UTP"));
        }
    }

    @GetMapping
    public List<UsuarioEntity> obtenerTodos() {
        return usuarioRepository.findAll();
    }
}
