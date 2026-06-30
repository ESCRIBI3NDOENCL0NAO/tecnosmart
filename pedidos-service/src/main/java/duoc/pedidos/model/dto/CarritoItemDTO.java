package duoc.pedidos.model.dto;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Long productoId;
    private Integer cantidad;
}
