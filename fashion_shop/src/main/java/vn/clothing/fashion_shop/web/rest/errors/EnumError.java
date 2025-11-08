package vn.clothing.fashion_shop.web.rest.errors;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumError {
    //----------------Product-------------------
    PRODUCT_DATA_EXISTED_EMAIL("PRODUCT-DTE-EMAIL","Product already exists with the given Email:",HttpStatus.CONFLICT),
    PRODUCT_ERR_NOT_FOUND_ID("PRODUCT-CATE_NF","Not found product with id:",HttpStatus.BAD_REQUEST),
    
    //----------------Role-------------------
    ROLE_DATA_EXISTED_NAME("ROLE-DTE-NAME","Role already exists with the given Name:",HttpStatus.CONFLICT),
    ROLE_ERR_NOT_FOUND_ID("ROLE-CATE_NF","Not found role with id:",HttpStatus.BAD_REQUEST),
    
    //----------------Server-------------------
    INTERNAL_ERROR("ACC-S-999", "Unexpected internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
    
    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    public static EnumError fromCode(String code) {
        for (EnumError e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown DispatchError code: " + code);
    }
}
