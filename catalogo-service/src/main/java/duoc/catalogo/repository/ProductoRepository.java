package duoc.catalogo.repository;

import duoc.catalogo.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// esta interfaz es el punto de contacto con la tabla 'productos'
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByCategoriaId(Long categoriaId);

    /*
    Explicación: recibe y valida los datos para luego guardarlos en la base de datos
    ademas de poder filtrar por categorías.
     */
}
