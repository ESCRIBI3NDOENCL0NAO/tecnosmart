package duoc.pedidos.service;

import duoc.pedidos.client.CarritoClient;
import duoc.pedidos.client.InventarioClient;
import duoc.pedidos.model.dto.CarritoItemDTO;
import duoc.pedidos.model.dto.PedidoRequestDTO;
import duoc.pedidos.model.entity.Pedido;
import duoc.pedidos.model.entity.PedidoItem;
import duoc.pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    // inyectamos a Feign
    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private CarritoClient carritoClient;

    @Transactional
    public Pedido procesarCompra(Long usuarioId) {
        log.info("Iniciando proceso de compra para el usuario ID: {}", usuarioId);
        // llamamos al carrito para ver lo que quiere comprar
        List<CarritoItemDTO> productosEnCarrito = carritoClient.obtenerCarrito(usuarioId);
        if (productosEnCarrito.isEmpty()) {
            log.warn("El carrito del usuario {} está vacío", usuarioId);
            throw new RuntimeException("No hay productos en el carrito para procesar");
        }

        // llamamos al inventario para ver el stock del producto
        for (CarritoItemDTO item : productosEnCarrito) {
            boolean hayStock = inventarioClient.validarStock(item.getProductoId(), item.getCantidad());
            if (!hayStock) {
                log.error("Stock insuficiente para el producto ID: {}", item.getProductoId());
                throw new RuntimeException("Stock insuficiente para el producto: " + item.getProductoId());
            }
        }
        // si está correcto, guardará el nuevo pedido en la base de datos
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuarioId(usuarioId);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado("PAGADO");
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        log.info("Pedido guardado con éxito, ID: {}", pedidoGuardado.getId());

        // luego, vaciara el carrito
        log.info("Vaciando carrito del usuario {}...", usuarioId);
        carritoClient.vaciarCarrito(usuarioId);
        return pedidoGuardado;
    }

    public List<Pedido> listarTodosLosPedidos() {
        log.info("Consultando historial completo de pedidos");
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        log.info("Buscando pedido con ID: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        log.info("Consultando pedidos del usuario ID: {}", usuarioId);
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // con esto se actualiza solo el estado del pedido
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pedido ID {} a {}", id, nuevoEstado);
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public void eliminarPedido(Long id) {
        log.warn("Eliminando pedido con ID: {}", id);
        Pedido pedido = buscarPorId(id);
        // al borrar el pedido, JPA borrará automáticamente sus PedidoItem gracias al CascadeType.ALL
        pedidoRepository.delete(pedido);
    }
}


/*
Explicación: comunica todos los servicios entre sí y se asegura de que si algo falla,
no se cobre ni cree el pedido
 */