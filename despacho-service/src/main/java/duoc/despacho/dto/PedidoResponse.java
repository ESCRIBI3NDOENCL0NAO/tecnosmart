package duoc.despacho.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private String estado; // Evaluaremos si dice "PAGADO"
}
