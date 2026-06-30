package duoc.pagos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notificaciones-service", path = "/api/notificaciones")
public interface NotificacionesClient {
    @PostMapping("/enviar")
    void enviarConfirmacion(@RequestParam String email, @RequestParam String mensaje);
}