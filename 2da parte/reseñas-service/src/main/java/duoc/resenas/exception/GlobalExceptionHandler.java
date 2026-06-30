package duoc.resenas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Se activa solo si el usuario falla una validación
    @ExceptionHandler(MethodArgumentNotValidException.class) // Atrapa errores de validación
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();

        // Extrae el mensaje de error exacto que escribiste en el Modelo
        String mensaje = ex.getBindingResult().getFieldError().getDefaultMessage();
        error.put("error", mensaje);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Retorna Error 400
    }
    // Se activa con errores generales del CRUD
    @ExceptionHandler(RuntimeException.class) // Atrapa errores de búsqueda (ID)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();

        // Extrae el mensaje "No se encontro el Pago con el ID: X"
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
