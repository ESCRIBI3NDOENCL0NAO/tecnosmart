package duoc.carrito.service;

import duoc.carrito.dto.CarritoRequestDTO;
import duoc.carrito.model.CarritoItem;
import duoc.carrito.repository.CarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarritoService {
    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    @Autowired
    private CarritoRepository carritoRepository;

    public CarritoItem agregarItem(CarritoRequestDTO dto) {
        log.info("Agregando producto {} al carrito del usuario {}", dto.getProductoId(), dto.getUsuarioId());
        return carritoRepository.findByUsuarioIdAndProductoId(dto.getUsuarioId(), dto.getProductoId())
                .map(itemExistente -> {
                    itemExistente.setCantidad(itemExistente.getCantidad() + dto.getCantidad());
                    return carritoRepository.save(itemExistente);
                })
                .orElseGet(() -> {
                    CarritoItem nuevoItem = new CarritoItem();
                    nuevoItem.setUsuarioId(dto.getUsuarioId());
                    nuevoItem.setProductoId(dto.getProductoId());
                    nuevoItem.setCantidad(dto.getCantidad());
                    return carritoRepository.save(nuevoItem);
                });
    }

    public List<CarritoItem> obtenerCarritoPorUsuario(Long usuarioId) {
        log.info("Consultando carrito del usuario ID: {}", usuarioId);
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem actualizarCantidad(Long id, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del item ID {} a {}", id, nuevaCantidad);

        if (nuevaCantidad == null || nuevaCantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        CarritoItem item = carritoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item del carrito no encontrado con ID: " + id));

        item.setCantidad(nuevaCantidad);
        return carritoRepository.save(item);
    }

    public void eliminarItem(Long id) {
        log.warn("Eliminando item ID {} del carrito", id);

        if (!carritoRepository.existsById(id)) {
            throw new NoSuchElementException("Item del carrito no encontrado con ID: " + id);
        }
        carritoRepository.deleteById(id);
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        log.warn("Vaciando carrito completo del usuario ID: {}", usuarioId);
        carritoRepository.deleteByUsuarioId(usuarioId);
    }
}
/* Explicación: en lugar de crear varias filas de un producto, las agrupa,
ádemas, limpia el carrito luego de una compra */