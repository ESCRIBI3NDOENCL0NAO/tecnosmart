package duoc.despacho.service;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.dto.PedidoResponse;
import duoc.despacho.entity.Despacho;
import duoc.despacho.repository.DespachoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
public class DespachoService {

    private static final Logger log = LoggerFactory.getLogger(DespachoService.class);
    private final DespachoRepository repository;
    private final WebClient.Builder webClientBuilder;

    public DespachoService(DespachoRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClientBuilder = webClientBuilder;
    }

    public Despacho programarDespacho(DespachoRequest request) {
        log.info("Iniciando validación remota para despacho del Pedido ID: {}", request.getPedidoId());

        // Consumo síncronizado vía Eureka al microservicio de pedidos
        PedidoResponse pedido = webClientBuilder.build().get()
                .uri("http://pedido-service/api/pedidos/{id}", request.getPedidoId())
                .retrieve()
                .bodyToMono(PedidoResponse.class)
                .block(); // .block() detiene el hilo para garantizar la secuencia del negocio

        if (pedido == null) {
            log.error("Error: El pedido {} no existe en TecnoSmart", request.getPedidoId());
            throw new RuntimeException("El pedido solicitado no existe en los registros centrales de TecnoSmart");
        }

        log.info("Pedido verificado correctamente. Estado actual: {}", pedido.getEstado());

        // Regla de Negocio Industrial: Solo despachar si el pedido ya fue pagado con éxito
        Despacho despacho = Despacho.builder()
                .pedidoId(request.getPedidoId())
                .direccion(request.getDireccion())
                .comuna(request.getComuna())
                .estado("EN_PREPARACION")
                .fechaProgramada(LocalDate.now().plusDays(3)) // Promesa de entrega estándar (3 días)
                .build();

        return repository.save(despacho);
    }
}
