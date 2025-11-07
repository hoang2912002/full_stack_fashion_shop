package vn.clothing.fashion_shop.constants.response;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;

    private String language;
    private String errorCode;
    private Map<String, Object> params;

    // ✅ SUCCESS (OK)
    public static <T> ApiResponse<T> success(T data) {
        return success("Success", data);
    }

    // ✅ SUCCESS with custom message
    public static <T> ApiResponse<T> success(String message, T data) {
        Locale locale = LocaleContextHolder.getLocale();
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .language(locale.getLanguage())
                .build();
    }

    // ✅ ERROR default (500)
    public static <T> ApiResponse<T> error(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(),message,locale.getLanguage(), null, null);
    }
    
    public static <T> ApiResponse<T> error(int status, String message) {
        Locale locale = LocaleContextHolder.getLocale();
        return error(status,message,locale.getLanguage(), null, null);
    }

    public static <T> ApiResponse<T> error(int status, String message, String language) {
        return error(status,message,language, null, null);
    }


    // ✅ ERROR overload without params
    public static <T> ApiResponse<T> error(int status, String message, String language, String errorCode) {
        return error(status, message, language, errorCode, null);
    }

    // ✅ ERROR with all params
    public static <T> ApiResponse<T> error(int status, String message, String language, String errorCode, Map<String, Object> params) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .language(language)
                .params(params)
                .build();
    }
}