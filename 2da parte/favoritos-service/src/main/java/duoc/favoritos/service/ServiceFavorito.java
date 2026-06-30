package duoc.favoritos.service;

import duoc.favoritos.model.ModelFavorito;
import duoc.favoritos.repository.RepositoryFavorito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceFavorito {

    @Autowired
    private RepositoryFavorito repository;

    // Toma el Favorito nuevo y lo manda a la base de datos
    public ModelFavorito guardar(ModelFavorito favorito) {
        return repository.save(favorito);
    }

    // Trae una lista con absolutamente todos los Favoritos registrados
    public List<ModelFavorito> obtenerTodos() {
        return repository.findAll();
    }

    // Busca un Favorito especifico. Si no lo encuentra, lanza una advertencia de error
    public ModelFavorito buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el favorito con ID: " + id));
    }

    // Busca el Favorito viejo, le pone los datos nuevos, y lo vuelve a guardar
    public ModelFavorito actualizar(Long id, ModelFavorito datosNuevos) {
        ModelFavorito existente = buscarPorId(id);

        existente.setUsuario(datosNuevos.getUsuario());
        existente.setTipoItem(datosNuevos.getTipoItem());
        existente.setNombreItem(datosNuevos.getNombreItem());

        return repository.save(existente);
    }

    // Busca el Favorito por ID y si existe lo borra
    public void eliminar(Long id) {
        ModelFavorito existente = buscarPorId(id);
        repository.delete(existente);
    }
}
