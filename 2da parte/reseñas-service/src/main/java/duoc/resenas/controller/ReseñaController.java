package duoc.resenas.controller;

import duoc.resenas.model.ModelReseña;
import duoc.resenas.service.ServiceReseña;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ReseñaController {

    @Autowired
    private ServiceReseña service;

    @PostMapping // Guardar datos
    public ModelReseña crear(@Valid @RequestBody ModelReseña resena) {
        return service.guardar(resena);
    }

    @GetMapping // Responde cuando Postman pide ver los datos GET
    public List<ModelReseña> listar() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}") // El {id} significa que espera un número al final de la URL
    public ModelReseña obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }


    @PutMapping("/{id}") // Responde cuando Postman pide modificar datos PUT
    public ModelReseña actualizar(@PathVariable Long id, @Valid @RequestBody ModelReseña resena) {
        return service.actualizar(id, resena);
    }


    @DeleteMapping("/{id}") // Responde cuando Postman pide borrar algo DELETE
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Reseña eliminada correctamente.";
    }

}