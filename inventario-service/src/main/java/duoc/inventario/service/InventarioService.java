package duoc.inventario.service;

import duoc.inventario.model.dto.InventarioRequestDTO;
import duoc.inventario.model.entity.Inventario;
import duoc.inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventarioService {
    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    public Inventario crearInventario(InventarioRequestDTO dto) {
        log.info("Creando nuevo registro de stock para el producto ID: {}", dto.getProductoId());
        // verifica que el producto no tenga ya un inventario
        if (inventarioRepository.findByProductoId(dto.getProductoId()).isPresent()) {
            log.error("El producto ID {} ya tiene stock registrado", dto.getProductoId());
            throw new RuntimeException("El producto ya tiene un registro de inventario. Use PUT para actualizar.");
        }

        Inventario i = new Inventario();
        i.setProductoId(dto.getProductoId());
        i.setCantidad(dto.getCantidad());

        return inventarioRepository.save(i);
    }

    public List<Inventario> listarTodoInventario() {
        log.info("Consultando el inventario general");
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Long id) {
        log.info("Buscando registro de inventario con ID: {}", id);
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de inventario no encontrado con ID: " + id));
    }

    public Inventario buscarStockPorProductoId(Long productoId) {
        log.info("Consultando stock actual del producto ID: {}", productoId);
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No hay registro de stock para el producto ID: " + productoId));
    }

    public Inventario actualizarInventario(Long id, InventarioRequestDTO dto) {
        log.info("Actualizando cantidad del inventario ID: {}", id);
        Inventario inventario = buscarPorId(id);
        // actualiza solo la cantidad
        inventario.setCantidad(dto.getCantidad());
        return inventarioRepository.save(inventario);
    }

    public void eliminarInventario(Long id) {
        log.warn("Eliminando registro de inventario con ID: {}", id);
        Inventario inventario = buscarPorId(id);
        inventarioRepository.delete(inventario);
    }

    public boolean validarStock(Long productoId, Integer cantidadRequerida) {
        log.info("Validando stock para producto ID: {}, cantidad requerida: {}", productoId, cantidadRequerida);

        return inventarioRepository.findByProductoId(productoId)
                .map(inventario -> inventario.getCantidad() >= cantidadRequerida)
                .orElse(false); // Si no existe el registro, retorna falso (no hay stock)
    }
}

/* Explicación: posee todas las funciones necesarias para manejar el stock
* correctamente, hasta buscar el stock correspondiente a un producto, lo que
* lo enlaza con 'catálogo' */
