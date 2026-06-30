package duoc.pedidos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId; // quien compra
    private Double total;   // cuanto pagó en total
    private LocalDateTime fecha; // cuando compró

    // un pedido tiene muchos items, 'cascade' asegura que si guardamos el pedido, se guarden sus items automáticamente
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoItem> items;

    private String estado;

    /*
    Explicación: pedido guarda los datos generales y con OneToMany, se puede ver
    todos los pedidos asociados
     */
}
