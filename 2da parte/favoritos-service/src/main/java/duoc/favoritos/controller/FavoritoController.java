package duoc.favoritos.controller;

import duoc.favoritos.model.ModelFavorito;
import duoc.favoritos.service.ServiceFavorito;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private ServiceFavorito service;

    @PostMapping // Guardar datos
    public ModelFavorito crear(@Valid @RequestBody ModelFavorito favorito) {
        return service.guardar(favorito);
    }

    @GetMapping // Responde cuando Postman pide ver los datos GET
    public List<ModelFavorito> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}") // El {id} significa que espera un número al final de la URL
    public ModelFavorito obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")  // Responde cuando Postman pide modificar datos PUT
    public ModelFavorito actualizar(@PathVariable Long id, @Valid @RequestBody ModelFavorito favorito) {
        return service.actualizar(id, favorito);
    }

    @DeleteMapping("/{id}") // Responde cuando Postman pide borrar algo DELETE
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Favorito eliminado correctamente.";
    }
}
