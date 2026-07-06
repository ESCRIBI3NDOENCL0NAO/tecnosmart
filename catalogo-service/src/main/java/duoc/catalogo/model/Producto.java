package duoc.catalogo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;

    // @ManyToOne indica que muchos productos pueden pertenecer a una categoría
    // @JoinColumn crea la llave foránea en la tabla MySQL llamada 'categoria_id'
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
/*
Explicación: es la entidad principal, y está atada a las categorías, por lo que
no puede existir sin ellas
 */