package duoc.usuarios.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import duoc.usuarios.model.Usuario;
import duoc.usuarios.dto.UsuarioRequestDTO;
import duoc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // esta clase manejará peticiones HTTP y devolverá JSON
@RequestMapping("/api/usuarios")// @RequestMapping define la (URL) para todos los endpoints de este archivo
public class UsuarioController {
    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    // @Autowired inyecta el servicio para delegarle la lógica de negocio
    @Autowired
    private UsuarioService usuarioService;

    // ejemplo de testing
    @GetMapping("/test")
    public String test() {
        log.info("Se ha recibido una petición en el endpoint de prueba /test");
        return "El Microservicio de Usuarios está funcionando correctamente.";
    }

    @PostMapping
    // @RequestBody convierte el JSON al DTO y @Valid activa las validaciones del DTO
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        // llama al servicio para que haga el registro
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuarioRequestDTO);
        // retorna el usuario creado con un código HTTP 201 (Created)
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    // lista a todos los usuarios guardados
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    // busca al usuario por su ID
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    // actualiza al usuario usando su ID
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioRequestDTO));
    }

    @DeleteMapping("/{id}")
    // elimina al usuario junto a su ID
    public  ResponseEntity<Usuario> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // retorna 204 no content
    }
}

/* Explicación: Controller gestiona el protocolo HTTP extrayendo la información del JSON y
validando que sea correcto para pasar a 'Service' o ser rechazado, este tiene 4 métodos:
POST(create), GET(read), PUT(update) y DELETE (CRUD) */