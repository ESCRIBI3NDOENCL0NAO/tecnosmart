package duoc.despacho;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.dto.PedidoResponse;
import duoc.despacho.model.Despacho;
import duoc.despacho.repository.DespachoRepository;
import duoc.despacho.service.DespachoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespachoServiceTest {

    @Mock
    private DespachoRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private DespachoService service;

    private void configurarWebClientConPedido(PedidoResponse pedido) {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PedidoResponse.class))
                .thenReturn(pedido != null ? Mono.just(pedido) : Mono.empty());
    }

    @Test
    void programarDespacho_debeGuardarDespachoEnPreparacionCuandoPedidoEstaPagado() {
        PedidoResponse pedidoPagado = new PedidoResponse();
        pedidoPagado.setId(1L);
        pedidoPagado.setEstado("PAGADO");
        configurarWebClientConPedido(pedidoPagado);

        DespachoRequest request = new DespachoRequest();
        request.setPedidoId(1L);
        request.setDireccion("Av. Providencia 123");
        request.setComuna("Providencia");

        when(repository.save(any(Despacho.class))).thenAnswer(i -> {
            Despacho d = i.getArgument(0);
            d.setId(1L);
            return d;
        });

        Despacho resultado = service.programarDespacho(request);

        assertEquals(1L, resultado.getPedidoId());
        assertEquals("EN_PREPARACION", resultado.getEstado());
        assertEquals("Providencia", resultado.getComuna());
        assertEquals(LocalDate.now().plusDays(3), resultado.getFechaProgramada());
        verify(repository).save(any(Despacho.class));
    }

    @Test
    void programarDespacho_debeLanzarExcepcionSiElPedidoNoExiste() {
        // WebClient retorna null cuando el pedido no se encuentra
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PedidoResponse.class)).thenReturn(Mono.empty());

        DespachoRequest request = new DespachoRequest();
        request.setPedidoId(99L);
        request.setDireccion("Calle Falsa 123");
        request.setComuna("Santiago");

        assertThrows(NoSuchElementException.class, () -> service.programarDespacho(request));
        verify(repository, never()).save(any());
    }

    @Test
    void programarDespacho_debeLanzarExcepcionSiPedidoNoEstaPagado() {
        PedidoResponse pedidoPendiente = new PedidoResponse();
        pedidoPendiente.setId(1L);
        pedidoPendiente.setEstado("PENDIENTE"); // no está pagado
        configurarWebClientConPedido(pedidoPendiente);

        DespachoRequest request = new DespachoRequest();
        request.setPedidoId(1L);
        request.setDireccion("Av. Las Condes 456");
        request.setComuna("Las Condes");

        assertThrows(IllegalStateException.class, () -> service.programarDespacho(request));
        verify(repository, never()).save(any());
    }


    @Test
    void listarTodos_debeRetornarListaDeDespachos() {
        Despacho despacho = Despacho.builder().id(1L).pedidoId(1L).estado("EN_PREPARACION").build();
        when(repository.findAll()).thenReturn(List.of(despacho));

        List<Despacho> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("EN_PREPARACION", resultado.get(0).getEstado());
        verify(repository).findAll();
    }

    @Test
    void listarTodos_debeRetornarListaVaciaSiNoHayDespachos() {
        when(repository.findAll()).thenReturn(List.of());

        List<Despacho> resultado = service.listarTodos();

        assertTrue(resultado.isEmpty());
    }


    @Test
    void buscarPorId_debeRetornarDespachoCuandoExiste() {
        Despacho despacho = Despacho.builder().id(1L).pedidoId(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(despacho));

        Despacho resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_debeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }


    @Test
    void buscarPorPedido_debeRetornarDespachoCuandoExiste() {
        Despacho despacho = Despacho.builder().id(1L).pedidoId(5L).estado("ENVIADO").build();
        when(repository.findByPedidoId(5L)).thenReturn(Optional.of(despacho));

        Despacho resultado = service.buscarPorPedido(5L);

        assertEquals(5L, resultado.getPedidoId());
        verify(repository).findByPedidoId(5L);
    }

    @Test
    void buscarPorPedido_debeLanzarExcepcionSiPedidoNoTieneDespacho() {
        when(repository.findByPedidoId(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorPedido(99L));
    }


    @Test
    void actualizarEstado_debeActualizarYGuardarNuevoEstado() {
        Despacho despacho = Despacho.builder().id(1L).estado("EN_PREPARACION").build();
        when(repository.findById(1L)).thenReturn(Optional.of(despacho));
        when(repository.save(despacho)).thenReturn(despacho);

        Despacho resultado = service.actualizarEstado(1L, "ENVIADO");

        assertEquals("ENVIADO", resultado.getEstado());
        verify(repository).save(despacho);
    }

    @Test
    void actualizarEstado_debeLanzarExcepcionSiDespachoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarEstado(99L, "ENVIADO"));
        verify(repository, never()).save(any());
    }


    @Test
    void eliminarDespacho_debeEliminarCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarDespacho(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarDespacho_debeLanzarExcepcionCuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarDespacho(99L));
        verify(repository, never()).deleteById(any());
    }
}