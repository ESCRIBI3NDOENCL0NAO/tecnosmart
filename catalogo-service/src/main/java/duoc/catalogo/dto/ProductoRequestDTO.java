package duoc.catalogo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoriaId; // solo pide el ID

    /*
    Explicación: sirve para mejorar la comunicación entre tablas, al no tener que pedir
    el objeto entero, sino solo su ID, y validad que sea un número positivo y que este
    presente antes de una solicitud de petición
     */
}