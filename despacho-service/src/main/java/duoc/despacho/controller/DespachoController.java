package duoc.despacho.controller;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.entity.Despacho;
import duoc.despacho.service.DespachoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final DespachoService service;

    @PostMapping("/")
    public ResponseEntity<Despacho> crear(@Valid @RequestBody DespachoRequest request) {
        Despacho programado = service.programarDespacho(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(programado);
    }
}