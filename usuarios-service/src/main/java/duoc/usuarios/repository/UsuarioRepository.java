package duoc.usuarios.repository;

import duoc.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email); // funciona como una consulta SQl (SELECT COUNT(*) FROM usuarios WHERE email = ?)

    /*
    Explicación: su función es hablar directamente con MySQL, y gracias a que hereda de JpaRepository
    la capacidad de guardar, borrar, actualizar y buscar registros, elimina la necesidad de escribir
    código en SQL directamente
     */
}