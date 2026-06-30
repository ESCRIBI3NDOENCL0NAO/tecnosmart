package duoc.pedidos.repository;

import duoc.pedidos.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}

/*
Explicación: permite guardar nuevas órdenes y recuperar las compras hechas por un cliente,
como si se tratase de un historial
 */