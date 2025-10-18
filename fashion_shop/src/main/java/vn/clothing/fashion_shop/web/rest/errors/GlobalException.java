package vn.clothing.fashion_shop.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            RuntimeException.class
    })
    public ResponseEntity<Object> handleAllException(Exception ex) {
        Object res = new Object();
        // res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // res.setMessage(ex.getMessage());
        // res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}