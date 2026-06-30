package duoc.notificaciones.service;

import duoc.notificaciones.model.ModelNotificacion;
import duoc.notificaciones.repository.RepositoryNotificacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionesServiceTest {

    @Mock
    private RepositoryNotificacion repository;

    @InjectMocks
    private ServiceNotificacion service;

    @Test
    void testGuardar() {
        ModelNotificacion noti = new ModelNotificacion();
        when(repository.save(any(ModelNotificacion.class))).thenReturn(noti);

        ModelNotificacion resultado = service.guardar(noti);

        assertNotNull(resultado);
        verify(repository, times(1)).save(noti);
    }

    @Test
    void testObtenerTodas() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<ModelNotificacion> resultado = service.obtenerTodas();
        assertNotNull(resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Long id = 1L;
        ModelNotificacion noti = new ModelNotificacion();
        when(repository.findById(id)).thenReturn(Optional.of(noti));

        ModelNotificacion resultado = service.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(noti, resultado);
    }

    @Test
    void testActualizar() {
        Long id = 1L;
        ModelNotificacion existente = new ModelNotificacion();
        ModelNotificacion nuevosDatos = new ModelNotificacion();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(ModelNotificacion.class))).thenReturn(existente);

        ModelNotificacion resultado = service.actualizar(id, nuevosDatos);

        assertNotNull(resultado);
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        ModelNotificacion existente = new ModelNotificacion();
        when(repository.findById(id)).thenReturn(Optional.of(existente));

        service.eliminar(id);

        verify(repository, times(1)).delete(existente);
    }
}