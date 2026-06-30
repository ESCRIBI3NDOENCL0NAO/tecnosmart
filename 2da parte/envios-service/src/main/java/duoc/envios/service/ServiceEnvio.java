package duoc.envios.service;

import duoc.envios.model.ModelEnvio;
import duoc.envios.repository.RepositoryEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceEnvio {

    @Autowired
    private RepositoryEnvio repository;

    // Toma el Envio nuevo y lo manda a la base de datos
    public ModelEnvio guardar(ModelEnvio envio) {
        return repository.save(envio);
    }

    // Trae una lista con absolutamente todos los Envios registrados
    public List<ModelEnvio> obtenerTodos() {
        return repository.findAll();
    }

    // Busca un Envio especifico. Si no lo encuentra, lanza una advertencia de error
    public ModelEnvio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el envío con el ID: " + id));
    }

    // Busca el Envio viejo, le pone los datos nuevos, y lo vuelve a guardar
    public ModelEnvio actualizar(Long id, ModelEnvio envioActualizado) {
        ModelEnvio envioExistente = buscarPorId(id);

        envioExistente.setDestinatario(envioActualizado.getDestinatario());
        envioExistente.setDireccionDestino(envioActualizado.getDireccionDestino());
        envioExistente.setEmpresaTransporte(envioActualizado.getEmpresaTransporte());
        envioExistente.setEstado(envioActualizado.getEstado());

        return repository.save(envioExistente);
    }

    // Busca el Envio por ID y si existe lo borra
    public void eliminar(Long id) {
        ModelEnvio envioExistente = buscarPorId(id);
        repository.delete(envioExistente);
    }
}
