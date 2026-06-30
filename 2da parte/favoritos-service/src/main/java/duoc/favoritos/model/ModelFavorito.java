package duoc.favoritos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Crea getters y setters
@Entity // Crea la tabla en MySQL
@Table(name = "favoritos")
public class ModelFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea 1, 2, 3... (Autoincrementable)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "Debes indicar qué tipo de favorito es (Ej: Empresa, Producto)")
    private String tipoItem;

    @NotBlank(message = "El nombre del favorito no puede estar vacío")
    private String nombreItem;

    /* Explicación: Esta tabla guardará las alertas del sistema.
    Usamos @NotBlank para asegurarnos de que no se envíen notificaciones en blanco.
    */
}
