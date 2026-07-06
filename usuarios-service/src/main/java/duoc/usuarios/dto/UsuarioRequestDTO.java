package duoc.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    // @NotBlank verifica que no haya espacios en blanco
    // @Size indica los mínimos y máximos de caracteres
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    // @Email pide un @ y un dominio válido
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // no es obligatorio, no requiere validaciones
    private String direccion;

    /*
    Explicación: el DTO es un guardia que aprueba o rechaza cualquier petición que no
    cumpla con las validaciones para proteger la información incompleta
    */
}