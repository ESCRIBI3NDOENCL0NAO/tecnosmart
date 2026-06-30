package duoc.inventario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // esta clase captura los errores de cualquier Controller

public class GlobalExceptionHandler {

    // MethodArgumentNotValidException ocurre cuando falla una validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // recorre todos los errores de validación encontrados
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage); // guarda: "nombre": "El nombre es obligatorio"
        });

        // retorna un JSON con los errores y un código HTTP 400 (Bad Request)
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // atrapa excepciones generales
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        // Retornamos un código HTTP 400 o 500 según corresponda.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /*
    Explicación: este código funciona como una red, atrapando cualquier error para que la base
    no se caiga, además, cuando captura los errores los explica (ej.: "El formato del email no es válido")
    asi entendemos el error y lo podemos corregir.
     */

}