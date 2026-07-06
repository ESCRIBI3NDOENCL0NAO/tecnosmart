package duoc.usuarios;

import duoc.usuarios.dto.UsuarioRequestDTO;
import duoc.usuarios.model.Usuario;
import duoc.usuarios.repository.UsuarioRepository;
import duoc.usuarios.service.UsuarioService;
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
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private UsuarioService service;

	@Test
	void crearUsuario_debeGuardarYRetornarUsuarioCuandoEmailNoExiste() {
		UsuarioRequestDTO dto = new UsuarioRequestDTO();
		dto.setNombre("Juan Pérez");
		dto.setEmail("juan@correo.com");
		dto.setPassword("1234");
		dto.setDireccion("Av. Siempre Viva 123");

		when(usuarioRepository.existsByEmail("juan@correo.com")).thenReturn(false);
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
			Usuario u = i.getArgument(0);
			u.setId(1L);
			return u;
		});

		Usuario resultado = service.crearUsuario(dto);

		assertEquals("Juan Pérez", resultado.getNombre());
		assertEquals("juan@correo.com", resultado.getEmail());
		assertNotNull(resultado.getId());
		verify(usuarioRepository).save(any(Usuario.class));
	}

	@Test
	void crearUsuario_debeLanzarExcepcionSiEmailYaEstaRegistrado() {
		UsuarioRequestDTO dto = new UsuarioRequestDTO();
		dto.setEmail("duplicado@correo.com");

		when(usuarioRepository.existsByEmail("duplicado@correo.com")).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> service.crearUsuario(dto));
		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void listarUsuarios_debeRetornarListaDeUsuarios() {
		Usuario u = new Usuario();
		u.setId(1L);
		u.setNombre("Ana Torres");
		when(usuarioRepository.findAll()).thenReturn(List.of(u));

		List<Usuario> resultado = service.listarUsuarios();

		assertEquals(1, resultado.size());
		assertEquals("Ana Torres", resultado.get(0).getNombre());
		verify(usuarioRepository).findAll();
	}

	@Test
	void listarUsuarios_debeRetornarListaVaciaSiNoHayUsuarios() {
		when(usuarioRepository.findAll()).thenReturn(List.of());

		List<Usuario> resultado = service.listarUsuarios();

		assertTrue(resultado.isEmpty());
	}

	@Test
	void buscarPorId_debeRetornarUsuarioCuandoExiste() {
		Usuario u = new Usuario();
		u.setId(1L);
		u.setNombre("Pedro Soto");
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

		Usuario resultado = service.buscarPorId(1L);

		assertEquals(1L, resultado.getId());
		assertEquals("Pedro Soto", resultado.getNombre());
		verify(usuarioRepository).findById(1L);
	}

	@Test
	void buscarPorId_debeLanzarExcepcionCuandoNoExiste() {
		when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
	}

	@Test
	void actualizarUsuario_debeActualizarDatosCorrectamente() {
		Usuario usuarioExistente = new Usuario();
		usuarioExistente.setId(1L);
		usuarioExistente.setNombre("Nombre Viejo");
		usuarioExistente.setEmail("viejo@correo.com");
		usuarioExistente.setDireccion("Calle Vieja 1");

		UsuarioRequestDTO dto = new UsuarioRequestDTO();
		dto.setNombre("Nombre Nuevo");
		dto.setEmail("viejo@correo.com"); // mismo email, no cambia
		dto.setDireccion("Calle Nueva 99");

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

		Usuario resultado = service.actualizarUsuario(1L, dto);

		assertEquals("Nombre Nuevo", resultado.getNombre());
		assertEquals("Calle Nueva 99", resultado.getDireccion());
		verify(usuarioRepository).save(usuarioExistente);
	}

	@Test
	void actualizarUsuario_debeLanzarExcepcionSiNuevoEmailYaEstaEnUso() {
		Usuario usuarioExistente = new Usuario();
		usuarioExistente.setId(1L);
		usuarioExistente.setEmail("actual@correo.com");

		UsuarioRequestDTO dto = new UsuarioRequestDTO();
		dto.setNombre("Nombre");
		dto.setEmail("ocupado@correo.com"); // este email ya lo usa otro usuario

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.existsByEmail("ocupado@correo.com")).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> service.actualizarUsuario(1L, dto));
		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void actualizarUsuario_debeLanzarExcepcionSiUsuarioNoExiste() {
		when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

		UsuarioRequestDTO dto = new UsuarioRequestDTO();
		dto.setNombre("Alguien");
		dto.setEmail("alguien@correo.com");

		assertThrows(NoSuchElementException.class, () -> service.actualizarUsuario(99L, dto));
		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void eliminarUsuario_debeEliminarCuandoExiste() {
		Usuario u = new Usuario();
		u.setId(1L);
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

		service.eliminarUsuario(1L);

		verify(usuarioRepository).delete(u);
	}

	@Test
	void eliminarUsuario_debeLanzarExcepcionSiUsuarioNoExiste() {
		when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.eliminarUsuario(99L));
		verify(usuarioRepository, never()).delete(any());
	}
}
