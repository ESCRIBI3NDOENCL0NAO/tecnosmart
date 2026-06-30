package duoc.carrito.repository;

import duoc.carrito.model.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    // ve todos los productos que tiene un usuario específico en su carrito
    List<CarritoItem> findByUsuarioId(Long usuarioId);

    // busca si el producto ya está en el carrito y solo suma la cantidad
    Optional<CarritoItem> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    // vacía el carrito después de una compra
    void deleteByUsuarioId(Long usuarioId);

    /*
    Explicación: permite filtrar que productos pertenecen a quien y actualizar
    las cantidades de ese producto sin tener que duplicar filas
     */
}