package br.com.marinadelara.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Classe para personalizar as mensagens de erro para o usuário. As mensagens serão mais amigáveis e intuitivas

@ControllerAdvice //anotação para definir classes globais no tratamento de exceção: toda exceção passa por aqui
public class ExceptionHandlerController {
    //Anotação para definir qual tipo de excessão deve passar por esse método
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
    }
}
