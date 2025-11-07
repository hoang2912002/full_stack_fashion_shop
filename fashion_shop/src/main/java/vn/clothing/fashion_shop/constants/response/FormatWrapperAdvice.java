package vn.clothing.fashion_shop.constants.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.constants.annotation.SkipWrapResponse;

@RestControllerAdvice
public class FormatWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        //áp dụng tất cả trừ annotation SkipWrapResponse
        return !returnType.hasMethodAnnotation(SkipWrapResponse.class);
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(
        @Nullable Object body, 
        MethodParameter returnType, 
        MediaType selectedContentType,
        Class selectedConverterType, 
        ServerHttpRequest request, 
        ServerHttpResponse response
    ) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        ApiMessageResponse messageResponse = returnType.getMethodAnnotation(ApiMessageResponse.class);
        String message = messageResponse != null ? messageResponse.value() : "CALL API SUCCESS";

        if(body == null || body instanceof ApiResponse || body instanceof String){
            return body;
        }
        if(status >= 400){
            return body;
        }
        return ApiResponse.success(message,body);
    }
    
}
