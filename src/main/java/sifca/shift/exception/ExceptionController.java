package sifca.shift.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sifca.shift.exception.modelsException.AccesException;
import sifca.shift.exception.modelsException.DatabaseException;
import sifca.shift.exception.modelsException.NotFoundException;
import sifca.shift.exception.modelsException.OrderException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<AwesomeException> handleNotFoundException() {
        return new ResponseEntity<>(new AwesomeException("Error ¯\\_(ツ)_/¯ "), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccesException.class)
    protected ResponseEntity<AwesomeException> handleAcсesException() {
        return new ResponseEntity<>(new AwesomeException("Error acсess"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderException.class)
    protected ResponseEntity<AwesomeException> handleOrderException() {
        return new ResponseEntity<>(new AwesomeException("Error order"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<AwesomeException> handleDatabaseException() {
        return new ResponseEntity<>(new AwesomeException("Error database"), HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeException {
        private String message;
    }
}