package duoc.favoritos.service;

import duoc.favoritos.model.ModelFavorito;
import duoc.favoritos.repository.RepositoryFavorito;
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
public class FavoritosServiceTest {

    @Mock
    private RepositoryFavorito repository;

    @InjectMocks
    private ServiceFavorito service;

    @Test
    void testGuardar() {
        ModelFavorito favorito = new ModelFavorito();
        when(repository.save(any(ModelFavorito.class))).thenReturn(favorito);

        ModelFavorito resultado = service.guardar(favorito);

        assertNotNull(resultado);
        verify(repository, times(1)).save(favorito);
    }

    @Test
    void testObtenerTodos() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<ModelFavorito> resultado = service.obtenerTodos();
        assertNotNull(resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Long id = 1L;
        ModelFavorito favorito = new ModelFavorito();
        when(repository.findById(id)).thenReturn(Optional.of(favorito));

        ModelFavorito resultado = service.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(favorito, resultado);
    }

    @Test
    void testActualizar() {
        Long id = 1L;
        ModelFavorito existente = new ModelFavorito();
        ModelFavorito nuevosDatos = new ModelFavorito();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(ModelFavorito.class))).thenReturn(existente);

        ModelFavorito resultado = service.actualizar(id, nuevosDatos);

        assertNotNull(resultado);
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        ModelFavorito existente = new ModelFavorito();
        when(repository.findById(id)).thenReturn(Optional.of(existente));

        service.eliminar(id);

        verify(repository, times(1)).delete(existente);
    }
}
