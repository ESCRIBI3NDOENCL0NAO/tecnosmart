package duoc.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Usamos el NOMBRE exacto con el que se registra Inventario en Eureka (suele ser inventario-service)
@FeignClient(name = "inventario-service")
public interface InventarioClient {

    // Copiamos EXACTAMENTE la misma firma del endpoint que hicimos en el InventarioController
    @GetMapping("/api/inventario/validar/{productoId}/{cantidad}")
    boolean validarStock(@PathVariable("productoId") Long productoId, @PathVariable("cantidad") Integer cantidad);
}