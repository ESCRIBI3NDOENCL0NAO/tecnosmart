package duoc.notificaciones.controller;

import duoc.notificaciones.model.ModelNotificacion;
import duoc.notificaciones.service.ServiceNotificacion;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones") // ¡Esta es la URL que pondrás en Postman!
public class NotificacionController {

    @Autowired
    private ServiceNotificacion service;

    @PostMapping // Guardar datos
    public ModelNotificacion crear(@Valid @RequestBody ModelNotificacion notificacion) {
        return service.guardar(notificacion);
    }

    @GetMapping // Responde cuando Postman pide ver los datos GET
    public List<ModelNotificacion> listar() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}") // El {id} significa que espera un número al final de la URL
    public ModelNotificacion obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}") // Responde cuando Postman pide modificar datos PUT
    public ModelNotificacion actualizar(@PathVariable Long id, @Valid @RequestBody ModelNotificacion notificacion) {
        return service.actualizar(id, notificacion);
    }

    @DeleteMapping("/{id}") // Responde cuando Postman pide borrar algo DELETE
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Notificación eliminada correctamente.";
    }
}
