package duoc.pedidos.dto;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Long productoId;
    private Integer cantidad;
}
