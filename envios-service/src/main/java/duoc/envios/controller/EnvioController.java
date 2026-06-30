package duoc.envios.controller;

import duoc.envios.model.ModelEnvio;
import duoc.envios.service.ServiceEnvio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private ServiceEnvio service;

    @PostMapping // Guardar datos
    public ModelEnvio crear(@Valid @RequestBody ModelEnvio envio) {
        return service.guardar(envio);
    }

    @GetMapping // Responde cuando Postman pide ver los datos GET
    public List<ModelEnvio> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}") // El {id} significa que espera un número al final de la URL
    public ModelEnvio obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}") // Responde cuando Postman pide modificar datos PUT
    public ModelEnvio actualizar(@PathVariable Long id, @Valid @RequestBody ModelEnvio envio) {
        return service.actualizar(id, envio);
    }

    @DeleteMapping("/{id}") // Responde cuando Postman pide borrar algo DELETE
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Envío eliminado correctamente.";
    }
}