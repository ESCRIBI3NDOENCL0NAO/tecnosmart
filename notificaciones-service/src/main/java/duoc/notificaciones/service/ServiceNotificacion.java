package duoc.notificaciones.service;

import duoc.notificaciones.model.ModelNotificacion;
import duoc.notificaciones.repository.RepositoryNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceNotificacion {

    @Autowired
    private RepositoryNotificacion repository;

    // Toma la Notificacion nuevo y lo manda a la base de datos
    public ModelNotificacion guardar(ModelNotificacion notificacion) {
        return repository.save(notificacion);
    }

    // Trae una lista con absolutamente todos las Notificaciones registrados
    public List<ModelNotificacion> obtenerTodas() {
        return repository.findAll();
    }

    // Busca una Notificacion especifico. Si no lo encuentra, lanza una advertencia de error
    public ModelNotificacion buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la notificación con ID: " + id));
    }

    // Busca la Notificacion vieja, le pone los datos nuevos, y lo vuelve a guardar
    public ModelNotificacion actualizar(Long id, ModelNotificacion datosNuevos) {
        ModelNotificacion notificacionExistente = buscarPorId(id);

        notificacionExistente.setUsuarioDestino(datosNuevos.getUsuarioDestino());
        notificacionExistente.setMensaje(datosNuevos.getMensaje());
        notificacionExistente.setTipoNotificacion(datosNuevos.getTipoNotificacion());

        return repository.save(notificacionExistente);
    }

    // Busca la Notificacion por ID y si existe lo borra
    public void eliminar(Long id) {
        ModelNotificacion notificacionExistente = buscarPorId(id);
        repository.delete(notificacionExistente);
    }
}
