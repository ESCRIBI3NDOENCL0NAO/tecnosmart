package duoc.catalogo.controller;

import duoc.catalogo.dto.ProductoRequestDTO;
import duoc.catalogo.model.Producto;
import duoc.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // crea un producto nuevo
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoRequestDTO dto) {
        return new ResponseEntity<>(productoService.guardarProducto(dto), HttpStatus.CREATED);
    }

    // lista todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarLosProductos());
    }

    // busca productos por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable long id) {
        return ResponseEntity.ok(productoService.buscarProductoPorId(id));
    }

    // actualiza los productos
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    // elimina los productos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}