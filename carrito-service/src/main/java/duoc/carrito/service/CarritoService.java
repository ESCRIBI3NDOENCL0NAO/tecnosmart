package duoc.carrito.service;

import duoc.carrito.model.dto.CarritoRequestDTO;
import duoc.carrito.model.entity.CarritoItem;
import duoc.carrito.repository.CarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarritoService {
    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    @Autowired
    private CarritoRepository carritoRepository;

    public CarritoItem agregarItem(CarritoRequestDTO dto) {
        log.info("Agregando producto {} al carrito del usuario {}", dto.getProductoId(), dto.getUsuarioId());
        // busca si el usuario ya tiene este producto en el carrito
        return carritoRepository.findByUsuarioIdAndProductoId(dto.getUsuarioId(), dto.getProductoId())
                .map(itemExistente -> {
                    // si ya existe, solo suma la cantidad
                    itemExistente.setCantidad(itemExistente.getCantidad() + dto.getCantidad());
                    return carritoRepository.save(itemExistente);
                })
                .orElseGet(() -> {
                    // si no existe, creamos un nuevo registro
                    CarritoItem nuevoItem = new CarritoItem();
                    nuevoItem.setUsuarioId(dto.getUsuarioId());
                    nuevoItem.setProductoId(dto.getProductoId());
                    nuevoItem.setCantidad(dto.getCantidad());
                    return carritoRepository.save(nuevoItem);
                });
    }

    // método conectado con pedidos
    public List<CarritoItem> obtenerCarritoPorUsuario(Long usuarioId) {
        log.info("Consultando carrito del usuario ID: {}", usuarioId);
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem actualizarCantidad(Long id, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del item ID {} a {}", id, nuevaCantidad);
        CarritoItem item = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));
        item.setCantidad(nuevaCantidad);
        return carritoRepository.save(item);
    }

    // elimina solo un item
    public void eliminarItem(Long id) {
        log.warn("Eliminando item ID {} del carrito", id);
        carritoRepository.deleteById(id);
    }

    // elimina el carrito por completo (método en pedidos)
    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        log.warn("Vaciando carrito completo del usuario ID: {}", usuarioId);
        carritoRepository.deleteByUsuarioId(usuarioId);
    }
}

/* Explicación: en lugar de crear varias filas de un producto, las agrupa,
ádemas, limpia el carrito luego de una compra */