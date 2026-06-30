package duoc.pagos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequest {
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodo;

    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Integer monto;
}