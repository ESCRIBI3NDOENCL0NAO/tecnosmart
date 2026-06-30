package duoc.catalogo.repository;

import duoc.catalogo.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// esta interfaz es el punto de contacto con la tabla 'categorías'
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);

    /*
    Explicación: gestiona las categorías en la base de datos y válida la existencia de ellas
    para no crear un producto sin pertenecer a una categoria.
    */

}