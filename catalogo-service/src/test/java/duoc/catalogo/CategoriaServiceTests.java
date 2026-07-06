package duoc.catalogo;

import duoc.catalogo.dto.CategoriaRequestDTO;
import duoc.catalogo.model.Categoria;
import duoc.catalogo.repository.CategoriaRepository;
import duoc.catalogo.service.CategoriaService;
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
class CategoriaServiceTest {

	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private CategoriaService service;

	@Test
	void guardarCategoria_debeGuardarYRetornarCategoriaCuandoNombreNoExiste() {
		CategoriaRequestDTO dto = new CategoriaRequestDTO();
		dto.setNombre("Laptops");
		dto.setDescripcion("Computadores portátiles");

		when(categoriaRepository.existsByNombre("Laptops")).thenReturn(false);
		when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> {
			Categoria c = i.getArgument(0);
			c.setId(1L);
			return c;
		});

		Categoria resultado = service.guardarCategoria(dto);

		assertEquals("Laptops", resultado.getNombre());
		assertEquals("Computadores portátiles", resultado.getDescripcion());
		assertNotNull(resultado.getId());
		verify(categoriaRepository).save(any(Categoria.class));
	}

	@Test
	void guardarCategoria_debeLanzarExcepcionSiNombreYaExiste() {
		CategoriaRequestDTO dto = new CategoriaRequestDTO();
		dto.setNombre("Laptops");
		dto.setDescripcion("Descripción cualquiera");

		when(categoriaRepository.existsByNombre("Laptops")).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> service.guardarCategoria(dto));
		verify(categoriaRepository, never()).save(any());
	}

	@Test
	void listarLasCategorias_debeRetornarListaDeCategorias() {
		Categoria c = new Categoria();
		c.setId(1L);
		c.setNombre("Monitores");
		when(categoriaRepository.findAll()).thenReturn(List.of(c));

		List<Categoria> resultado = service.listarLasCategorias();

		assertEquals(1, resultado.size());
		assertEquals("Monitores", resultado.get(0).getNombre());
		verify(categoriaRepository).findAll();
	}

	@Test
	void listarLasCategorias_debeRetornarListaVaciaSiNoHayCategorias() {
		when(categoriaRepository.findAll()).thenReturn(List.of());

		List<Categoria> resultado = service.listarLasCategorias();

		assertTrue(resultado.isEmpty());
	}

	@Test
	void buscarCategoriasPorId_debeRetornarCategoriaCuandoExiste() {
		Categoria c = new Categoria();
		c.setId(1L);
		c.setNombre("Periféricos");
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(c));

		Categoria resultado = service.buscarCategoriasPorId(1L);

		assertEquals(1L, resultado.getId());
		assertEquals("Periféricos", resultado.getNombre());
		verify(categoriaRepository).findById(1L);
	}

	@Test
	void buscarCategoriasPorId_debeLanzarExcepcionCuandoNoExiste() {
		when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.buscarCategoriasPorId(99L));
	}
}