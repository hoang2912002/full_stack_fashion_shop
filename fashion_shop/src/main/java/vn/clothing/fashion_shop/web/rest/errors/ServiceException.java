package vn.clothing.fashion_shop.web.rest.errors;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final EnumError enumError;
    private final String errorCode;
    private final String messageCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> params;

    public ServiceException(EnumError error){
        this(error, error.name().toLowerCase(), null);
    }
    public ServiceException(EnumError error,String messageCode ){
        this(error, messageCode, null);
    }

    public ServiceException(EnumError error,String messageCode, Map<String, Object> params){
        super(error.getDefaultMessage());
        this.enumError = error;
        this.errorCode = error.getCode();
        this.messageCode = messageCode;
        this.httpStatus = error.getHttpStatus();
        this.params = params;
    }
}
