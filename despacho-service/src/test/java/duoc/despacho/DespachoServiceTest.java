package duoc.despacho;

import duoc.despacho.dto.DespachoRequest;
import duoc.despacho.dto.PedidoResponse;
import duoc.despacho.entity.Despacho;
import duoc.despacho.repository.DespachoRepository;
import duoc.despacho.service.DespachoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private DespachoService service;

    @BeforeEach
    void setUp() {
        // Configuramos el encadenamiento fluido de WebClient de forma limpia
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void programarDespacho_CuandoPedidoExiste_RetornaDespachoExitoso() {
        // 1. GIVEN
        DespachoRequest request = new DespachoRequest(100L, "Av. Vitacura 1234", "Vitacura");
        PedidoResponse pedidoSimulado = new PedidoResponse(100L, 12L, "PAGADO");

        Despacho despachoGuardadoSimulado = Despacho.builder()
                .id(1L)
                .pedidoId(100L)
                .direccion("Av. Vitacura 1234")
                .comuna("Vitacura")
                .estado("EN_PREPARACION")
                .fechaProgramada(LocalDate.now().plusDays(3))
                .build();

        when(responseSpec.bodyToMono(PedidoResponse.class)).thenReturn(Mono.just(pedidoSimulado));
        when(repository.save(any(Despacho.class))).thenReturn(despachoGuardadoSimulado);

        // 2. WHEN
        Despacho resultado = service.programarDespacho(request);

        // 3. THEN
        assertNotNull(resultado, "El despacho no debería ser nulo");
        assertEquals(1L, resultado.getId());
        assertEquals("EN_PREPARACION", resultado.getEstado());
        assertEquals(100L, resultado.getPedidoId());

        verify(repository, times(1)).save(any(Despacho.class));
    }

    @Test
    void programarDespacho_CuandoPedidoNoExiste_LanzaRuntimeException() {
        // 1. GIVEN
        DespachoRequest request = new DespachoRequest(999L, "Alameda 456", "Santiago");

        when(responseSpec.bodyToMono(PedidoResponse.class)).thenReturn(Mono.empty());

        // 2. WHEN & THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            service.programarDespacho(request);
        });

        assertTrue(excepcion.getMessage().contains("no existe en los registros centrales de TecnoSmart"));
        verify(repository, never()).save(any(Despacho.class));
    }
}