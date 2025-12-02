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
    PRODUCT_DATA_EXISTED_APPROVAL_PENDING("PRODUCT-DTE-APPROVAL-PENDING","Product already exists with the pending approval request:",HttpStatus.CONFLICT),
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
    PERMISSION_ACCESS_DENIED("PERMISSION-ACCESS-DENIED","You do not have permission to access this resource.",HttpStatus.FORBIDDEN),

    //----------------Option-------------------
    OPTION_DATA_EXISTED_NAME("OPTION-DTE-NAME","Option already exists with the given Name:",HttpStatus.CONFLICT),
    OPTION_ERR_NOT_FOUND_ID("OPTION-CATE_NF","Not found option with id:",HttpStatus.BAD_REQUEST),
    
    //----------------Option value-------------------
    OPTION_VALUE_DATA_EXISTED_VALUE("OPTION-VALUE-DTE-VALUE","Option value already exists with the given Value:",HttpStatus.CONFLICT),
    OPTION_VALUE_ERR_NOT_FOUND_ID("OPTION-VALUE-CATE_NF","Not found option value with id:",HttpStatus.BAD_REQUEST),

    //----------------Coupon-------------------
    COUPON_ERR_NOT_FOUND_ID("COUPON-CATE_NF","Not found coupon with id:",HttpStatus.CONFLICT),
    COUPON_DATA_EXISTED_CODE("COUPON-DTE-CODE","Coupon already exists with the given Code:",HttpStatus.CONFLICT),

    //----------------Inventory-------------------
    INVENTORY_ERR_NOT_FOUND_ID("INVENTORY-CATE_NF","Not found inventory with id:",HttpStatus.CONFLICT),
    INVENTORY_DATA_EXISTED_CODE("INVENTORY-DTE-CODE","Inventory already exists with the given Code:",HttpStatus.CONFLICT),
    INVENTORY_DATA_EXISTED_PRODUCT_ID( "INVENTORY-DTE-PRODUCT-ID","Inventory already exists with the given Product ID:",HttpStatus.CONFLICT),
    INVENTORY_ERR_NOT_FOUND_PRODUCT_ID( "INVENTORY-CATE_NF-PRODUCT-ID","Not found inventory with Product ID:",HttpStatus.CONFLICT),
    INVENTORY_ERR_NOT_UPDATE_STATUS_APPROVED("INVENTORY-ERR-NOT-UPDATE-STATUS-APPROVED","Cannot update inventory when product is in APPROVED status:",HttpStatus.CONFLICT),
    INVENTORY_INVALID_QUANTITY_AVAILABLE("INVENTORY-INVALID-QUANTITY-AVAILABLE","Inventory quantity available must not be negative number", HttpStatus.CONFLICT),
    //----------------ApprovalMaster-------------------
    APPROVAL_MASTER_ERR_NOT_FOUND_ID("APPROVAL-MASTER-CATE_NF","Not found approval master with id:",HttpStatus.CONFLICT),
    APPROVAL_MASTER_DATA_EXISTED_ENTITY_TYPE_STATUS_STEP("APPROVAL-MASTER-DTE-ENTITY_TYPE_STATUS_STEP","Approval master already exists with the given EntityType, Status, and Step:",HttpStatus.CONFLICT),
    APPROVAL_MASTER_DATA_STATUS_REJECTED_CANNOT_ADD_HISTORY("APPROVAL-MASTER-DTE-STATUS-REJECTED-CANNOT-ADD-HISTORY","Cannot add approval history to an approval master with REJECTED status:",HttpStatus.CONFLICT),
    APPROVAL_MASTER_ERR_NOT_FOUND_ENTITY_TYPE_STATUS("APPROVAL-MASTER-CATE_NF-ENTITY_TYPE_STATUS","Not found approval master with EntityType and Status:",HttpStatus.CONFLICT),
    APPROVAL_MASTER_ERR_NOT_FOUND_ENTITY_STATUS("APPROVAL-MASTER-CATE_NF-ENTITY_STATUS","Not found approval master with EntityType and Status:",HttpStatus.CONFLICT),
    //----------------ApprovalHistory-------------------
    APPROVAL_HISTORY_ERR_NOT_FOUND_ID("APPROVAL-HISTORY-CATE_NF","Not found approval history with id:",HttpStatus.CONFLICT),
    APPROVAL_HISTORY_CURRENT_ERR_MATCHING("APPROVAL-HISTORY-CURRENT-ERR-MATCHING","Last approval history not matching with current approval history", HttpStatus.CONFLICT),

    //----------------ShopManagement-------------------
    SHOP_MANAGEMENT_DATA_EXISTED_NAME("SHOP-MANAGEMENT-DTE-NAME","Shop management already exists with Name", HttpStatus.CONFLICT),
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
