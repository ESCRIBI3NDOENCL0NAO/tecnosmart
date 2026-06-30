package duoc.pagos.service;

import duoc.pagos.model.ModelPago;
import duoc.pagos.repository.RepositoryPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePago {

    @Autowired
    private RepositoryPago repository;

    // Toma el pago nuevo y lo manda a la base de datos
    public ModelPago guardar(ModelPago pago) {
        return repository.save(pago);
    }

    // Trae una lista con absolutamente todos los pagos registrados
    public List<ModelPago> obtenerTodos() {
        return repository.findAll();
    }

    // Busca un pago específico. Si no lo encuentra, lanza una advertencia de error
    public ModelPago buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pago con el ID: " + id));
    }

    // Busca el pago viejo, le pone los datos nuevos, y lo vuelve a guardar
    public ModelPago actualizar(Long id, ModelPago pagoActualizado) {
        ModelPago pagoExistente = buscarPorId(id);

        pagoExistente.setCliente(pagoActualizado.getCliente());
        pagoExistente.setMonto(pagoActualizado.getMonto());
        pagoExistente.setMetodoPago(pagoActualizado.getMetodoPago());

        return repository.save(pagoExistente);
    }

    // Busca el pago por ID y si existe lo borra
    public void eliminar(Long id) {
        ModelPago pagoExistente = buscarPorId(id);
        repository.delete(pagoExistente);
    }
}
