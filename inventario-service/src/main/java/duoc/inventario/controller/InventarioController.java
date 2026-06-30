package duoc.inventario.controller;

import duoc.inventario.model.dto.InventarioRequestDTO;
import duoc.inventario.model.entity.Inventario;
import duoc.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<Inventario> crear(@Valid @RequestBody InventarioRequestDTO dto) {
        return new ResponseEntity<>(inventarioService.crearInventario(dto), HttpStatus.CREATED); // 201 Created
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        return ResponseEntity.ok(inventarioService.listarTodoInventario());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> obtenerPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarStockPorProductoId(productoId));
    }

    @GetMapping("/validar/{productoId}/{cantidad}")
    public ResponseEntity<Boolean> validarStock(@PathVariable Long productoId, @PathVariable Integer cantidad) {
        boolean hayStock = inventarioService.validarStock(productoId, cantidad);
        return ResponseEntity.ok(hayStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable long id, @Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.ok(inventarioService.actualizarInventario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}


/* Explicación: permite un manejo total del inventario, con su CRUD completo */
