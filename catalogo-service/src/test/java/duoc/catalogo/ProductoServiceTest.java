package duoc.catalogo;

import duoc.catalogo.dto.ProductoRequestDTO;
import duoc.catalogo.model.Categoria;
import duoc.catalogo.model.Producto;
import duoc.catalogo.repository.CategoriaRepository;
import duoc.catalogo.repository.ProductoRepository;
import duoc.catalogo.service.ProductoService;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    // ProductoService también usa CategoriaRepository para validar que la categoría exista
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService service;

    @Test
    void guardarProducto_debeGuardarProductoCuandoCategoriaExiste() {
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Laptops");

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("MacBook Pro");
        dto.setDescripcion("Laptop Apple M3");
        dto.setPrecio(1499990.0);
        dto.setCategoriaId(1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> {
            Producto p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        Producto resultado = service.guardarProducto(dto);

        assertEquals("MacBook Pro", resultado.getNombre());
        assertEquals(1499990.0, resultado.getPrecio());
        assertEquals("Laptops", resultado.getCategoria().getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void guardarProducto_debeLanzarExcepcionSiCategoriaNoExiste() {
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Teclado");
        dto.setCategoriaId(99L); // categoría inexistente

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.guardarProducto(dto));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void listarLosProductos_debeRetornarListaDeProductos() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Monitor 4K");
        when(productoRepository.findAll()).thenReturn(List.of(p));

        List<Producto> resultado = service.listarLosProductos();

        assertEquals(1, resultado.size());
        assertEquals("Monitor 4K", resultado.get(0).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    void listarLosProductos_debeRetornarListaVaciaSiNoHayProductos() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = service.listarLosProductos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarProductoPorId_debeRetornarProductoCuandoExiste() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Mouse Gamer");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        Producto resultado = service.buscarProductoPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Mouse Gamer", resultado.getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void buscarProductoPorId_debeLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarProductoPorId(99L));
    }

    @Test
    void actualizarProducto_debeActualizarDatosYCategoria() {
        Categoria categoriaVieja = new Categoria();
        categoriaVieja.setId(1L);
        categoriaVieja.setNombre("Laptops");

        Categoria categoriaNueva = new Categoria();
        categoriaNueva.setId(2L);
        categoriaNueva.setNombre("Accesorios");

        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Nombre Viejo");
        productoExistente.setPrecio(100.0);
        productoExistente.setCategoria(categoriaVieja);

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Nombre Nuevo");
        dto.setDescripcion("Descripción nueva");
        dto.setPrecio(200.0);
        dto.setCategoriaId(2L); // cambia de categoría

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoriaNueva));
        when(productoRepository.save(productoExistente)).thenReturn(productoExistente);

        Producto resultado = service.actualizarProducto(1L, dto);

        assertEquals("Nombre Nuevo", resultado.getNombre());
        assertEquals(200.0, resultado.getPrecio());
        assertEquals("Accesorios", resultado.getCategoria().getNombre());
        verify(productoRepository).save(productoExistente);
    }

    @Test
    void actualizarProducto_debeLanzarExcepcionSiProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setCategoriaId(1L);

        assertThrows(NoSuchElementException.class, () -> service.actualizarProducto(99L, dto));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizarProducto_debeLanzarExcepcionSiNuevaCategoriaNOExiste() {
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Producto");
        dto.setCategoriaId(99L); // categoría inexistente

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarProducto(1L, dto));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void eliminarProducto_debeEliminarCuandoExiste() {
        Producto p = new Producto();
        p.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        service.eliminarProducto(1L);

        verify(productoRepository).delete(p);
    }

    @Test
    void eliminarProducto_debeLanzarExcepcionSiProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.eliminarProducto(99L));
        verify(productoRepository, never()).delete(any());
    }
}