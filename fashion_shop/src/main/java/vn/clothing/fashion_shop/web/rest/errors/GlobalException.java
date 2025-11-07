package vn.clothing.fashion_shop.web.rest.errors;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.response.ApiResponse;
import vn.clothing.fashion_shop.constants.util.MessageUtil;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {
    private final MessageUtil messageUtil;
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            RuntimeException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiResponse> handleAllException(Exception ex) {
        ApiResponse res = new ApiResponse<>();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        FieldError fieldError = fieldErrors.getFirst();

        String messageCode = fieldError.getDefaultMessage(); // ví dụ: "user.email.notnull"
        String fieldName = fieldError.getField();

        String message = messageUtil.getMessage(messageCode, locale);

        ApiResponse<Object> res = ApiResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .language(locale.getLanguage())
                .errorCode(messageCode)
                .params(Map.of(fieldName, message))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<ApiResponse> handleServiceException(ServiceException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        // Lấy EnumError từ exception
        EnumError enumError = ex.getEnumError();

        ApiResponse<Object> res = ApiResponse.builder()
            .status(enumError.getHttpStatus().value())
            .message(messageUtil.getMessage(ex.getMessageCode(), locale))
            .language(locale.getLanguage())
            .errorCode(ex.getMessageCode())
            .params(ex.getParams())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }


    @ExceptionHandler(value = {
        PermissionException.class
    })
    public ResponseEntity<ApiResponse> handlePermissionException(Exception ex) {
        ApiResponse res = new ApiResponse<>();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}