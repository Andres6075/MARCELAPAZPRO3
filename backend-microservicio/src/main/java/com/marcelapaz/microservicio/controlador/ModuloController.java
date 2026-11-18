package com.marcelapaz.microservicio.controlador;

import com.marcelapaz.microservicio.modelo.ModuloEntity;
import com.marcelapaz.microservicio.repositorio.ModuloRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@CrossOrigin(origins = "*")
public class ModuloController {

    private final ModuloRepository moduloRepository;

    public ModuloController(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
        sembrarDatosIniciales();
    }

    private void sembrarDatosIniciales() {
        if (moduloRepository.count() == 0) {
            moduloRepository.save(new ModuloEntity(null, "Comprensión y Narrativa Literaria", "Lectura guiada de textos breves, fábulas y cuentos.", "Tercero Básico", "Lenguaje y Comunicación", "Unidad 1: Descubriendo Historias", "OA 3: Leer literatura", "OA-LENG-03-03", "Lectura", "Lee atentamente la fábula.", true, 3, 1, "Media", 0.85f, false, 6, true));
            moduloRepository.save(new ModuloEntity(null, "Números Hasta el 1000 y Adición", "Resolución de problemas con números de tres cifras.", "Tercero Básico", "Matemática", "Unidad 1: Números y Operaciones", "OA 4: Representar números", "OA-MAT-03-04", "Números", "Utiliza la tabla posicional.", true, 3, 2, "Media", 0.50f, false, 8, true));
            moduloRepository.save(new ModuloEntity(null, "Los Seres Vivos y sus Hábitats", "Exploración de plantas y animales nativos de Chile.", "Tercero Básico", "Ciencias Naturales", "Unidad 1: El Entorno Natural", "OA 1: Observar animales nativos", "OA-CIE-03-01", "Ciencias de la Vida", "Observa las imágenes.", false, 2, 1, "Baja", 0.30f, false, 10, true));
            moduloRepository.save(new ModuloEntity(null, "Paisajes de Chile y Ubicación", "Reconocimiento de paisajes de Chile mediante mapas.", "Tercero Básico", "Historia, Geografía y Cs. Sociales", "Unidad 1: Descubriendo Paisajes", "OA 8: Describir paisajes", "OA-HIS-03-08", "Geografía", "Identifica características geográficas.", true, 3, 3, "Media", 1.0f, true, 5, true));
        }
    }

    @GetMapping
    public List<ModuloEntity> obtenerTodos() {
        return moduloRepository.findAll();
    }

    @PostMapping
    public ModuloEntity crear(@RequestBody ModuloEntity modulo) {
        return moduloRepository.save(modulo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuloEntity> actualizar(@PathVariable Integer id, @RequestBody ModuloEntity modulo) {
        return moduloRepository.findById(id).map(existente -> {
            modulo.setId(id);
            return ResponseEntity.ok(moduloRepository.save(modulo));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (moduloRepository.existsById(id)) {
            moduloRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
