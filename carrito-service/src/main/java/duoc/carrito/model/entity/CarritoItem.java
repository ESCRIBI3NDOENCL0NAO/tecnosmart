package duoc.carrito.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carrito_items")
@Data
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al usuario del MS-Usuarios
    @Column(nullable = false)
    private Long usuarioId;

    // Referencia al producto del MS-Catálogo
    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    /*
    Explicación: vincula a un usuario con un producto y cuantas unidades quiere
     */
}