package sifca.shift.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<AwesomeResponse> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new AwesomeResponse(exception.getMsg()), HttpStatus.NOT_FOUND);
        //return new ResponseEntity<>(new AwesomeResponse("Error ¯\\_(ツ)_/¯ "), HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeResponse {
        private String message;
    }
}