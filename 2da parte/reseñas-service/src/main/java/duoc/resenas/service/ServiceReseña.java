package duoc.resenas.service;

import duoc.resenas.model.ModelReseña;
import duoc.resenas.repository.RepositoryReseña;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceReseña {

    @Autowired
    private RepositoryReseña repository;
    // Toma el Pagos nuevo y lo manda a la base de datos
    public ModelReseña guardar(ModelReseña resena) {
        return repository.save(resena);
    }
    // Trae una lista con absolutamente todos los Pagos registrados
    public List<ModelReseña> obtenerTodas() {
        return repository.findAll();
    }
    // Busca el Pago especifico. Si no lo encuentra, lanza una advertencia de error
    public ModelReseña buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la reseña con el ID: " + id));
    }

    // Busca el Pago viejo, le pone los datos nuevos, y lo vuelve a guardar
    public ModelReseña actualizar(Long id, ModelReseña resenaActualizada) {
        ModelReseña resenaExistente = buscarPorId(id);

        resenaExistente.setEmpresaNombre(resenaActualizada.getEmpresaNombre());
        resenaExistente.setComentario(resenaActualizada.getComentario());
        resenaExistente.setCalificacion(resenaActualizada.getCalificacion());

        return repository.save(resenaExistente);
    }
    // Busca el Pago por ID y si existe lo borra
    public void eliminar(Long id) {
        ModelReseña resenaExistente = buscarPorId(id);
        repository.delete(resenaExistente);
    }

}
