package duoc.inventario;

import duoc.inventario.dto.InventarioRequestDTO;
import duoc.inventario.model.Inventario;
import duoc.inventario.repository.InventarioRepository;
import duoc.inventario.service.InventarioService;
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
class InventarioServiceTest {

	@Mock
	private InventarioRepository inventarioRepository;

	@InjectMocks
	private InventarioService service;

	@Test
	void crearInventario_debeGuardarRegistroCuandoProductoNoTieneStock() {
		InventarioRequestDTO dto = new InventarioRequestDTO();
		dto.setProductoId(10L);
		dto.setCantidad(50);

		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.empty());
		when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> {
			Inventario inv = i.getArgument(0);
			inv.setId(1L);
			return inv;
		});

		Inventario resultado = service.crearInventario(dto);

		assertEquals(10L, resultado.getProductoId());
		assertEquals(50, resultado.getCantidad());
		assertNotNull(resultado.getId());
		verify(inventarioRepository).save(any(Inventario.class));
	}

	@Test
	void crearInventario_debeLanzarExcepcionSiProductoYaTieneInventario() {
		InventarioRequestDTO dto = new InventarioRequestDTO();
		dto.setProductoId(10L);
		dto.setCantidad(20);

		Inventario inventarioExistente = new Inventario();
		inventarioExistente.setProductoId(10L);
		inventarioExistente.setCantidad(100);
		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventarioExistente));

		assertThrows(IllegalArgumentException.class, () -> service.crearInventario(dto));
		verify(inventarioRepository, never()).save(any());
	}

	@Test
	void listarTodoInventario_debeRetornarListaDeRegistros() {
		Inventario inv = new Inventario();
		inv.setId(1L);
		inv.setProductoId(10L);
		inv.setCantidad(30);
		when(inventarioRepository.findAll()).thenReturn(List.of(inv));

		List<Inventario> resultado = service.listarTodoInventario();

		assertEquals(1, resultado.size());
		assertEquals(10L, resultado.get(0).getProductoId());
		verify(inventarioRepository).findAll();
	}

	@Test
	void listarTodoInventario_debeRetornarListaVaciaSiNoHayRegistros() {
		when(inventarioRepository.findAll()).thenReturn(List.of());

		List<Inventario> resultado = service.listarTodoInventario();

		assertTrue(resultado.isEmpty());
	}

	@Test
	void buscarPorId_debeRetornarInventarioCuandoExiste() {
		Inventario inv = new Inventario();
		inv.setId(1L);
		inv.setProductoId(10L);
		inv.setCantidad(25);
		when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inv));

		Inventario resultado = service.buscarPorId(1L);

		assertEquals(1L, resultado.getId());
		assertEquals(25, resultado.getCantidad());
		verify(inventarioRepository).findById(1L);
	}

	@Test
	void buscarPorId_debeLanzarExcepcionCuandoNoExiste() {
		when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
	}

	@Test
	void buscarStockPorProductoId_debeRetornarInventarioCuandoExiste() {
		Inventario inv = new Inventario();
		inv.setProductoId(10L);
		inv.setCantidad(40);
		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inv));

		Inventario resultado = service.buscarStockPorProductoId(10L);

		assertEquals(10L, resultado.getProductoId());
		assertEquals(40, resultado.getCantidad());
		verify(inventarioRepository).findByProductoId(10L);
	}

	@Test
	void buscarStockPorProductoId_debeLanzarExcepcionSiProductoNoTieneStock() {
		when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.buscarStockPorProductoId(99L));
	}

	@Test
	void actualizarInventario_debeActualizarCantidadCorrectamente() {
		Inventario inv = new Inventario();
		inv.setId(1L);
		inv.setProductoId(10L);
		inv.setCantidad(10);

		InventarioRequestDTO dto = new InventarioRequestDTO();
		dto.setCantidad(99);

		when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inv));
		when(inventarioRepository.save(inv)).thenReturn(inv);

		Inventario resultado = service.actualizarInventario(1L, dto);

		assertEquals(99, resultado.getCantidad());
		verify(inventarioRepository).save(inv);
	}

	@Test
	void actualizarInventario_debeLanzarExcepcionSiRegistroNoExiste() {
		when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

		InventarioRequestDTO dto = new InventarioRequestDTO();
		dto.setCantidad(10);

		assertThrows(NoSuchElementException.class, () -> service.actualizarInventario(99L, dto));
		verify(inventarioRepository, never()).save(any());
	}

	@Test
	void eliminarInventario_debeEliminarCuandoExiste() {
		Inventario inv = new Inventario();
		inv.setId(1L);
		when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inv));

		service.eliminarInventario(1L);

		verify(inventarioRepository).delete(inv);
	}

	@Test
	void eliminarInventario_debeLanzarExcepcionSiRegistroNoExiste() {
		when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.eliminarInventario(99L));
		verify(inventarioRepository, never()).delete(any());
	}

	@Test
	void validarStock_debeRetornarTrueCuandoHaySuficienteStock() {
		Inventario inv = new Inventario();
		inv.setProductoId(10L);
		inv.setCantidad(50); // hay 50 unidades
		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inv));

		boolean resultado = service.validarStock(10L, 30); // piden 30

		assertTrue(resultado);
	}

	@Test
	void validarStock_debeRetornarFalseCuandoNoHaySuficienteStock() {
		Inventario inv = new Inventario();
		inv.setProductoId(10L);
		inv.setCantidad(5); // solo hay 5 unidades
		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inv));

		boolean resultado = service.validarStock(10L, 10); // piden 10

		assertFalse(resultado);
	}

	@Test
	void validarStock_debeRetornarFalseCuandoProductoNoExisteEnInventario() {
		when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

		boolean resultado = service.validarStock(99L, 1);

		assertFalse(resultado);
	}

	@Test
	void validarStock_debeRetornarTrueCuandoStockEsExactamenteElRequerido() {
		Inventario inv = new Inventario();
		inv.setProductoId(10L);
		inv.setCantidad(5); // exactamente 5
		when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inv));

		boolean resultado = service.validarStock(10L, 5); // piden exactamente 5

		assertTrue(resultado);
	}
}