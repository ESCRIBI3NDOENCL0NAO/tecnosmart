package duoc.resenas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "resenas")
public class ModelReseña {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea 1, 2, 3... (Autoincrementable)
    private Long id;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String empresaNombre;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(min = 10, max = 500, message = "El comentario debe tener entre 10 y 500 caracteres")
    private String comentario;

    // @Min y @Max definen el rango permitido para la nota
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private int calificacion;

    /* Explicación: Esta tabla guardará las alertas del sistema.
    Usamos @NotBlank para asegurarnos de que no se envíen notificaciones en blanco.
    */
}