package duoc.pedidos.client;

import duoc.pedidos.model.dto.CarritoItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Usamos el NOMBRE exacto con el que se registra Carrito en Eureka (suele ser carrito-service)
@FeignClient(name = "carrito-service")
public interface CarritoClient {

    // Llama al GET del carrito para ver qué tiene el usuario
    @GetMapping("/api/carrito/{usuarioId}")
    List<CarritoItemDTO> obtenerCarrito(@PathVariable("usuarioId") Long usuarioId);

    // Llama al DELETE del carrito para vaciarlo una vez que la compra es exitosa
    @DeleteMapping("/api/carrito/vaciar/{usuarioId}")
    void vaciarCarrito(@PathVariable("usuarioId") Long usuarioId);
}