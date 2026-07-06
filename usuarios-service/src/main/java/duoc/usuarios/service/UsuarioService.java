package duoc.usuarios.service;

import duoc.usuarios.model.Usuario;
import duoc.usuarios.dto.UsuarioRequestDTO;
import duoc.usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        log.info("Registrando nuevo usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", dto.getEmail());
            throw new IllegalArgumentException("El email ya está registrado en el sistema.");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setPassword(dto.getPassword());
        nuevoUsuario.setDireccion(dto.getDireccion());
        return usuarioRepository.save(nuevoUsuario);
    }

    public List<Usuario> listarUsuarios() {
        log.info("Buscando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("Buscando el usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario con ID " + id + " no encontrado"));
    }

    public Usuario actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando el usuario con id: {}", id);
        Usuario u = buscarPorId(id);

        if (!u.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de actualización con email ya registrado: {}", dto.getEmail());
            throw new IllegalArgumentException("El email ya está en uso por otro usuario.");
        }

        u.setNombre(dto.getNombre());
        u.setEmail(dto.getEmail());
        u.setDireccion(dto.getDireccion());
        return usuarioRepository.save(u);
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando el usuario con id: {}", id);
        Usuario u = buscarPorId(id);
        usuarioRepository.delete(u);
    }
}