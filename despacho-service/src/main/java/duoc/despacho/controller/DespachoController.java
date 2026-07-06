package duoc.despacho.controller;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.model.Despacho;
import duoc.despacho.service.DespachoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final DespachoService service;

    @PostMapping
    public ResponseEntity<Despacho> programarDespacho(@Valid @RequestBody DespachoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.programarDespacho(request));
    }

    @GetMapping
    public ResponseEntity<List<Despacho>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despacho> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Despacho> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.buscarPorPedido(pedidoId));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Despacho> actualizarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        return ResponseEntity.ok(service.actualizarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDespacho(@PathVariable Long id) {
        service.eliminarDespacho(id);
        return ResponseEntity.noContent().build();
    }
}