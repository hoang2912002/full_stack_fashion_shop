package vn.clothing.fashion_shop.web.rest.errors;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumError {
    //----------------User-------------------
    USER_DATA_EXISTED_EMAIL("USER-DTE-EMAIL","User already exists with the given Email:",HttpStatus.CONFLICT),
    USER_DATA_EXISTED_NAME("USER-DTE-EMAIL","User already exists with the given Email:",HttpStatus.CONFLICT),
    USER_ERR_NOT_FOUND_ID("USER-CATE_NF","Not found user with id:",HttpStatus.BAD_REQUEST),
    USER_ERR_NOT_FOUND_EMAIL("USER-CATE_NF-EMAIL","Not found user with email:",HttpStatus.BAD_REQUEST),
    USER_INVALID_REFRESH_TOKEN("USER-INVALID-REFRESH_TOKEN","User already with invalid Refresh token:",HttpStatus.BAD_REQUEST),
    
    //----------------Product-------------------
    PRODUCT_DATA_EXISTED_NAME("PRODUCT-DTE-NAME","Product already exists with the given Name:",HttpStatus.CONFLICT),
    PRODUCT_ERR_NOT_FOUND_ID("PRODUCT-CATE_NF","Not found product with id:",HttpStatus.BAD_REQUEST),
   
    //----------------Promotion-------------------
    PROMOTION_DATA_EXISTED_CODE("PROMOTION-DTE-CODE","Promotion already exists with the given Code:",HttpStatus.CONFLICT),
    PROMOTION_ERR_NOT_FOUND_ID("PROMOTION-CATE_NF","Not found promotion with id:",HttpStatus.BAD_REQUEST),
   
    //----------------Category-------------------
    CATEGORY_DATA_EXISTED_ID("CATEGORY-DTE-ID","Category already exists with the given Id:",HttpStatus.CONFLICT),
    CATEGORY_DATA_EXISTED_SLUG("CATEGORY-DTE-SLUG","Category already exists with the given Slug:",HttpStatus.CONFLICT),
    CATEGORY_ERR_NOT_FOUND_ID("CATEGORY-CATE_NF","Not found category with id:",HttpStatus.BAD_REQUEST),

    // PRODUCT_DATA_EXISTED_NAME("PRODUCT-DTE-NAME","Product already exists with the given Name:",HttpStatus.CONFLICT),

    //----------------Role-------------------
    ROLE_DATA_EXISTED_NAME("ROLE-DTE-NAME","Role already exists with the given Name:",HttpStatus.CONFLICT),
    ROLE_ERR_NOT_FOUND_ID("ROLE-CATE_NF","Not found role with id:",HttpStatus.BAD_REQUEST),
    
    //----------------Category-------------------
    CATEGORY_INVALID_ID("CATEGORY-INVALID-ID","Category already with invalid id:",HttpStatus.CONFLICT),


    //----------------Permission-------------------
    PERMISSION_DATA_EXISTED_APIPATH("PERMISSION-DTE-APIPATH","Permission already exists with the given Api path:",HttpStatus.CONFLICT),
    PERMISSION_ERR_NOT_FOUND_ID("PERMISSION-CATE_NF","Not found permission with id:",HttpStatus.BAD_REQUEST),

    //----------------Option-------------------
    OPTION_DATA_EXISTED_NAME("OPTION-DTE-NAME","Option already exists with the given Name:",HttpStatus.CONFLICT),
    OPTION_ERR_NOT_FOUND_ID("OPTION-CATE_NF","Not found option with id:",HttpStatus.BAD_REQUEST),
    
    //----------------Option value-------------------
    OPTION_VALUE_DATA_EXISTED_VALUE("OPTION-VALUE-DTE-VALUE","Option value already exists with the given Value:",HttpStatus.CONFLICT),
    OPTION_VALUE_ERR_NOT_FOUND_ID("OPTION-VALUE-CATE_NF","Not found option value with id:",HttpStatus.BAD_REQUEST),

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
