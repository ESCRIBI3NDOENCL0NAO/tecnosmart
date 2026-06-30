package duoc.pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos")
public class ModelPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea 1, 2, 3... (Autoincrementable)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String cliente;

    @NotBlank(message = "Debes especificar qué producto o servicio se está pagando")
    private String producto;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto mínimo a pagar es 1")
    private Integer monto;

    @NotBlank(message = "Debes especificar el método de pago (Ej: Tarjeta, Efectivo, Transferencia)")
    private String metodoPago;

    /* Explicación: Esta tabla guardará las alertas del sistema.
    Usamos @NotBlank para asegurarnos de que no se envíen notificaciones en blanco.
    */
}