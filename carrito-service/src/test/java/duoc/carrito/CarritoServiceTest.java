package duoc.carrito;

import duoc.carrito.dto.CarritoRequestDTO;
import duoc.carrito.model.CarritoItem;
import duoc.carrito.repository.CarritoRepository;
import duoc.carrito.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService service;

    @Test
    void agregarItem_debeCrearNuevoItemCuandoProductoNoEstaEnCarrito() {
        CarritoRequestDTO dto = new CarritoRequestDTO();
        dto.setUsuarioId(1L);
        dto.setProductoId(10L);
        dto.setCantidad(2);

        // el usuario NO tiene ese producto en el carrito todavía
        when(carritoRepository.findByUsuarioIdAndProductoId(1L, 10L)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(CarritoItem.class))).thenAnswer(i -> i.getArgument(0));

        CarritoItem resultado = service.agregarItem(dto);

        assertEquals(1L, resultado.getUsuarioId());
        assertEquals(10L, resultado.getProductoId());
        assertEquals(2, resultado.getCantidad());
        verify(carritoRepository).save(any(CarritoItem.class));
    }

    @Test
    void agregarItem_debeSumarCantidadCuandoProductoYaExisteEnCarrito() {
        CarritoRequestDTO dto = new CarritoRequestDTO();
        dto.setUsuarioId(1L);
        dto.setProductoId(10L);
        dto.setCantidad(3);

        // el usuario YA tiene ese producto con cantidad 2
        CarritoItem itemExistente = new CarritoItem();
        itemExistente.setId(1L);
        itemExistente.setUsuarioId(1L);
        itemExistente.setProductoId(10L);
        itemExistente.setCantidad(2);

        when(carritoRepository.findByUsuarioIdAndProductoId(1L, 10L)).thenReturn(Optional.of(itemExistente));
        when(carritoRepository.save(itemExistente)).thenReturn(itemExistente);

        CarritoItem resultado = service.agregarItem(dto);

        // 2 existentes + 3 nuevos = 5
        assertEquals(5, resultado.getCantidad());
        verify(carritoRepository).save(itemExistente);
    }

    @Test
    void obtenerCarritoPorUsuario_debeRetornarListaDeItemsDelUsuario() {
        CarritoItem item = new CarritoItem();
        item.setUsuarioId(1L);
        item.setProductoId(10L);
        item.setCantidad(1);

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(item));

        List<CarritoItem> resultado = service.obtenerCarritoPorUsuario(1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getProductoId());
        verify(carritoRepository).findByUsuarioId(1L);
    }

    @Test
    void obtenerCarritoPorUsuario_debeRetornarListaVaciaSiUsuarioNoTieneItems() {
        when(carritoRepository.findByUsuarioId(99L)).thenReturn(List.of());

        List<CarritoItem> resultado = service.obtenerCarritoPorUsuario(99L);

        assertTrue(resultado.isEmpty());
        verify(carritoRepository).findByUsuarioId(99L);
    }

    @Test
    void actualizarCantidad_debeActualizarYRetornarItemCuandoExiste() {
        CarritoItem item = new CarritoItem();
        item.setId(1L);
        item.setCantidad(2);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(item));
        when(carritoRepository.save(item)).thenReturn(item);

        CarritoItem resultado = service.actualizarCantidad(1L, 5);

        assertEquals(5, resultado.getCantidad());
        verify(carritoRepository).save(item);
    }

    @Test
    void actualizarCantidad_debeLanzarExcepcionCuandoItemNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarCantidad(99L, 3));
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void actualizarCantidad_debeLanzarExcepcionSiCantidadEsCero() {
        assertThrows(IllegalArgumentException.class, () -> service.actualizarCantidad(1L, 0));
        verify(carritoRepository, never()).findById(any());
    }

    @Test
    void actualizarCantidad_debeLanzarExcepcionSiCantidadEsNegativa() {
        assertThrows(IllegalArgumentException.class, () -> service.actualizarCantidad(1L, -1));
        verify(carritoRepository, never()).findById(any());
    }

    @Test
    void eliminarItem_debeEliminarCuandoItemExiste() {
        when(carritoRepository.existsById(1L)).thenReturn(true);

        service.eliminarItem(1L);

        verify(carritoRepository).deleteById(1L);
    }

    @Test
    void eliminarItem_debeLanzarExcepcionCuandoItemNoExiste() {
        when(carritoRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarItem(99L));
        verify(carritoRepository, never()).deleteById(any());
    }

    @Test
    void vaciarCarrito_debeLlamarDeleteByUsuarioId() {
        service.vaciarCarrito(1L);

        verify(carritoRepository).deleteByUsuarioId(1L);
    }
}