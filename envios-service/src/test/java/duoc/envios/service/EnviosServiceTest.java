package duoc.envios.service;

import duoc.envios.model.ModelEnvio;
import duoc.envios.repository.RepositoryEnvio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnviosServiceTest {

    @Mock
    private RepositoryEnvio repository;

    @InjectMocks
    private ServiceEnvio service;

    @Test
    void testGuardarEnvio() {
        ModelEnvio envio = new ModelEnvio();
        when(repository.save(any(ModelEnvio.class))).thenReturn(envio);

        ModelEnvio resultado = service.guardar(envio); // Ajusta el nombre si tu método es distinto

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(ModelEnvio.class));
    }

    @Test
    void testObtenerTodosEnvios() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        var resultado = service.obtenerTodos(); // Ajusta el nombre si tu método es distinto

        assertNotNull(resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarEnvioPorId() {
        Long id = 1L;
        ModelEnvio envio = new ModelEnvio();
        when(repository.findById(id)).thenReturn(Optional.of(envio));

        ModelEnvio resultado = service.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(envio, resultado);
    }

    @Test
    void testActualizarEnvio() {
        Long id = 1L;
        ModelEnvio envioExistente = new ModelEnvio();
        ModelEnvio envioNuevo = new ModelEnvio();

        when(repository.findById(id)).thenReturn(Optional.of(envioExistente));
        when(repository.save(any(ModelEnvio.class))).thenReturn(envioExistente);

        ModelEnvio resultado = service.actualizar(id, envioNuevo);

        assertNotNull(resultado);
        verify(repository, times(1)).save(envioExistente);
    }

    @Test
    void testEliminarEnvio() {
        Long id = 1L;
        ModelEnvio envio = new ModelEnvio();
        when(repository.findById(id)).thenReturn(Optional.of(envio));

        service.eliminar(id);

        verify(repository, times(1)).delete(envio);
    }
}