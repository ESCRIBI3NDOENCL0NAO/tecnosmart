package duoc.pagos.controller;

import duoc.pagos.dto.PagoRequest;
import duoc.pagos.entity.Pago;
import duoc.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor // Inyecta automáticamente el PagoService mediante el constructor
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);
    private final PagoService service; // Ya NO inyectamos el Repository directamente aquí

    @PostMapping("/")
    public ResponseEntity<Pago> registrarPago(@Valid @RequestBody PagoRequest request) {
        log.info("[Controller] Recibida petición HTTP POST para registrar pago");

        // El controlador solo coordina: recibe el DTO, llama al servicio y responde HTTP 201
        Pago nuevoPago = service.procesarPago(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }
}