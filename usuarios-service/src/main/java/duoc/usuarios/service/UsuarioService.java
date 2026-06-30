package duoc.usuarios.service;

import duoc.usuarios.model.entity.Usuario;
import duoc.usuarios.model.dto.UsuarioRequestDTO;
import duoc.usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    // @Autowired le pide a Spring que traiga el repositorio para usar sus métodos
    @Autowired
    private UsuarioRepository usuarioRepository;

    // recibimos el DTO (los datos limpios) y prometemos devolver una Entidad (el registro guardado)
    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        log.info("Registrando nuevo usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", dto.getEmail());
            // si el correo existe, se corta el código y lanza un error
            throw new RuntimeException("El email ya está registrado en el sistema.");
        }
        // crea una entidad vacía que representará nuestra nueva fila en MySQL
        // pasa los datos del DTO a la Entidad uno por uno
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setPassword(dto.getPassword());
        nuevoUsuario.setDireccion(dto.getDireccion());
        // usa 'save()' del repositorio para ejecutar el INSERT en la base de datos
        // retorna el objeto guardado con su id incluida en SQL
        return usuarioRepository.save(nuevoUsuario);
    }

    // lista de todos los usuarios añadidos
    public List<Usuario> listarUsuarios() {
        log.info("Buscando todos los usuarios");
        // usa 'findALl' para buscar todos los datos de la base
        return usuarioRepository.findAll();
    }

    // buscador de usuarios por su ID
    public Usuario buscarPorId(Long id) {
        log.info("Buscando el usuario con id: {}", id);
        // usa 'finById' para buscar específicamente al usuario que corresponde ese ID
        return usuarioRepository.findById(id).
                // si no lo encuentra, devuelve el siguiente mensaje:
                orElseThrow(() -> new RuntimeException("Usuario con ID " + id  + "no encontrado"));
    }

    public Usuario actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando el usuario con id: {}", id);
        // usa la lógica de búsqueda
        Usuario u = buscarPorId(id);
        // cambia todos los datos, a excepción de la contraseña
        u.setNombre(dto.getNombre());
        u.setDireccion(dto.getDireccion());
        u.setEmail(dto.getEmail());
        // guarda los nuevos usuarios en la base de datos
        return usuarioRepository.save(u);
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando el usuario con id: {}", id);
        Usuario u = buscarPorId(id);
        usuarioRepository.delete(u);
    }

    /*
    Explicación: Service es quien toma las decisiones, como recibir los datos del DTO,
    verificar las validaciones, transformar la información para que la base de datos la entienda
    y guardar la información en 'Repository'.
     */
}