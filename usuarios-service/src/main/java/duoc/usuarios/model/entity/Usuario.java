package duoc.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios") // es el nombre que tendrá la tabla en la base de datos
@Data // crea getters, setters y toString automáticamente
@NoArgsConstructor // crea un constructor sin argumentos
@AllArgsConstructor // crea un constructor con todos sus argumentos

public class Usuario {

    @Id // llave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // el ID es auto incrementable
    private Long id;

    // la fila agregada no puede ser nula y tiene un tamaño máximo de 100 caracteres
    @Column(nullable = false, length = 100)
    private String nombre;

    // la fila agregada no puede ser nula ni replicarse, además de tener un tamaño máximo de 100 caracteres
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // la fila agregada no puede ser nula
    @Column(nullable = false)
    private String password;

    // sin @Column, la fila no tendrá validaciones
    private String direccion;

    /*
    explicación del código: esta clase sirve como un plano de construcción, ya que le dice a
    Spring como debe crear la tabla 'usuarios' en MySQL, con sus nombres y validaciones.
    */
}