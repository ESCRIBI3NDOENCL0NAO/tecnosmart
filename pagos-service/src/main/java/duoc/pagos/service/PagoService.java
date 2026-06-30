package duoc.pagos.service;

import duoc.pagos.dto.PagoRequest;
import duoc.pagos.entity.Pago;
import duoc.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository repository;

    @Transactional // Asegura la integridad de la transacción en la base de datos
    public Pago procesarPago(PagoRequest request) {
        log.info("[Service] Iniciando procesamiento de pago para Pedido ID: {}", request.getPedidoId());

        // Aquí se encapsula la regla de negocio de TecnoSmart
        Pago pago = Pago.builder()
                .pedidoId(request.getPedidoId())
                .metodo(request.getMetodo())
                .monto(request.getMonto())
                .estado("APROBADO") // En el futuro, aquí conectarías con la pasarela real
                .fechaPago(LocalDateTime.now())
                .build();

        Pago pagoGuardado = repository.save(pago);
        log.info("[Service] Pago registrado exitosamente en la base de datos con ID: {}", pagoGuardado.getId());

        return pagoGuardado;
    }
}