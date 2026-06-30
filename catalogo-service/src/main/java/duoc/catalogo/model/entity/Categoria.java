package duoc.catalogo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String descripcion;

    /*
    Explicación: este código cataloga y permite que no haya productos sueltos, sino que todos
    pertenezcan a una categoria, y además permite crear nuevas categorías sin
    afectar a los productos.
     */
}