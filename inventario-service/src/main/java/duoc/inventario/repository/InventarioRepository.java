package duoc.inventario.repository;

import duoc.inventario.model.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // busca en el stock por el ID del producto
    // Optional está para manejar los casos donde no existe stock de un producto
    Optional<Inventario> findByProductoId(Long productoId);

    /*
    Explicación: permite guardar y encontrar rápido cualquier producto usando su ID
     */
}