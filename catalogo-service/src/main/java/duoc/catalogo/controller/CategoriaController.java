package duoc.catalogo.controller;

import duoc.catalogo.model.dto.CategoriaRequestDTO;
import duoc.catalogo.model.entity.Categoria;
import duoc.catalogo.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias") // ruta exclusiva para categorías
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // crea una categoria
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequestDTO dto) {
        return new ResponseEntity<>(categoriaService.guardarCategoria(dto), HttpStatus.CREATED);
    }

    // lista todas las categorías
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarLasCategorias());
    }

    // busca una categoria por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoriasPorId(id));
    }
}