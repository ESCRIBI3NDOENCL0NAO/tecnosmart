package duoc.carrito.controller;

import duoc.carrito.model.dto.CarritoRequestDTO;
import duoc.carrito.model.entity.CarritoItem;
import duoc.carrito.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping
    public ResponseEntity<CarritoItem> agregar(@Valid @RequestBody CarritoRequestDTO dto) {
        return new ResponseEntity<>(carritoService.agregarItem(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoItem> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    // vaciar carrito
    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}

/* Explicación: permite ver el carrito, añadir y/o borrar cosas del carrito */

