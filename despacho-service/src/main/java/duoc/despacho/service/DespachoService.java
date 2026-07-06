package duoc.despacho.service;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.dto.PedidoResponse;
import duoc.despacho.model.Despacho;
import duoc.despacho.repository.DespachoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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

        PedidoResponse pedido = webClientBuilder.build().get()
                .uri("http://pedido-service/api/pedidos/{id}", request.getPedidoId())
                .retrieve()
                .bodyToMono(PedidoResponse.class)
                .block();

        if (pedido == null) {
            log.error("Error: El pedido {} no existe", request.getPedidoId());
            throw new NoSuchElementException("El pedido solicitado no existe: " + request.getPedidoId());
        }

        if (!"PAGADO".equals(pedido.getEstado())) {
            log.error("El pedido {} no está en estado PAGADO, estado actual: {}", request.getPedidoId(), pedido.getEstado());
            throw new IllegalStateException("Solo se puede despachar un pedido en estado PAGADO. Estado actual: " + pedido.getEstado());
        }

        log.info("Pedido verificado correctamente. Estado: {}", pedido.getEstado());

        Despacho despacho = Despacho.builder()
                .pedidoId(request.getPedidoId())
                .direccion(request.getDireccion())
                .comuna(request.getComuna())
                .estado("EN_PREPARACION")
                .fechaProgramada(LocalDate.now().plusDays(3))
                .build();

        return repository.save(despacho);
    }

    public List<Despacho> listarTodos() {
        log.info("Consultando todos los despachos");
        return repository.findAll();
    }

    public Despacho buscarPorId(Long id) {
        log.info("Buscando despacho con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Despacho no encontrado con ID: " + id));
    }

    public Despacho buscarPorPedido(Long pedidoId) {
        log.info("Buscando despacho del pedido ID: {}", pedidoId);
        return repository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("No existe despacho para el pedido ID: " + pedidoId));
    }

    public Despacho actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del despacho ID {} a {}", id, nuevoEstado);
        Despacho despacho = buscarPorId(id);
        despacho.setEstado(nuevoEstado);
        return repository.save(despacho);
    }

    public void eliminarDespacho(Long id) {
        log.warn("Eliminando despacho con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Despacho no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}
