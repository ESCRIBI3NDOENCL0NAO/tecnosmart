package duoc.pagos;

import duoc.pagos.dto.PagoRequest;
import duoc.pagos.entity.Pago;
import duoc.pagos.repository.PagoRepository;
import duoc.pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository repository;

    @InjectMocks
    private PagoService service;

    @Test
    void procesarPago_DeberiaRegistrarYAprobarElPago() {
        // GIVEN
        PagoRequest request = new PagoRequest(1L, "REDCOMPRA", 25000);
        Pago pagoSimulado = Pago.builder()
                .id(99L)
                .pedidoId(1L)
                .metodo("REDCOMPRA")
                .monto(25000)
                .estado("APROBADO")
                .build();

        when(repository.save(any(Pago.class))).thenReturn(pagoSimulado);

        // WHEN
        Pago resultado = service.procesarPago(request);

        // THEN
        assertNotNull(resultado);
        assertEquals(99L, resultado.getId());
        assertEquals("APROBADO", resultado.getEstado());
        assertEquals(25000, resultado.getMonto());

        // Verifica que se interactuó con la BD exactamente una vez
        verify(repository, times(1)).save(any(Pago.class));
    }
}