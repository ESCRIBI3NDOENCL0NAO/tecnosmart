package duoc.notificaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "notificaciones")
public class ModelNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El usuario destino no puede estar vacío")
    private String usuarioDestino;

    @NotBlank(message = "El mensaje de la notificación es obligatorio")
    private String mensaje;

    @NotBlank(message = "Debes indicar el tipo (Ej: EMAIL, SMS, PUSH)")
    private String tipoNotificacion;

    /* Explicación: Esta tabla guardará las alertas del sistema.
    Usamos @NotBlank para asegurarnos de que no se envíen notificaciones en blanco.
    */
}
