package duoc.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;
}

/* Explicación: el sistema solo necesita el ID del usuario para buscar el carrito
y finalizar su compra  */