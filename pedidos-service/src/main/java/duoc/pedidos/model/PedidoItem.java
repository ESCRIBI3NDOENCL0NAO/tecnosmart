package duoc.pedidos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pedido_items")
@Data
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId; // quien compra
    private Integer cantidad; // cantidad de productos
    private Double precioUnitario; // precio de cada producto

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnore // Evita bucles infinitos al convertir a JSON
    private Pedido pedido;
}
/* Explicación: pedidoItem desglosa lo que compro y a que precio en cada pedido */
