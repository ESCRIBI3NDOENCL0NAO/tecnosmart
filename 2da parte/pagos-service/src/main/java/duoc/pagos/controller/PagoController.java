package duoc.pagos.controller;

import duoc.pagos.model.ModelPago;
import duoc.pagos.service.ServicePago;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private ServicePago service;

    @PostMapping // Guardar datos
    public ModelPago crear(@Valid @RequestBody ModelPago pago) {
        return service.guardar(pago);
    }

    @GetMapping // Responde cuando Postman pide ver los datos GET
    public List<ModelPago> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}") // El {id} significa que espera un número al final de la URL
    public ModelPago obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}") // Responde cuando Postman pide modificar datos PUT
    public ModelPago actualizar(@PathVariable Long id, @Valid @RequestBody ModelPago pago) {
        return service.actualizar(id, pago);
    }

    @DeleteMapping("/{id}") // Responde cuando Postman pide borrar algo DELETE
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Pago eliminado correctamente.";
    }
}
