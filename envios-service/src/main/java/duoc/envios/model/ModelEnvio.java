package duoc.envios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "envios")
public class ModelEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea 1, 2, 3... (Autoincrementable)
    private Long id;

    @NotBlank(message = "El nombre del destinatario es obligatorio")
    private String destinatario;

    @NotBlank(message = "Debes ingresar una dirección de destino")
    private String direccionDestino;

    @NotBlank(message = "Falta la empresa de transporte (Ej: Starken, Chilexpress)")
    private String empresaTransporte;

    @NotBlank(message = "Debes indicar el estado (Ej: En preparación, Despachado, Entregado)")
    private String estado;
    /* Explicación: Esta tabla guardará las alertas del sistema.
    Usamos @NotBlank para asegurarnos de que no se envíen notificaciones en blanco.
    */
}
