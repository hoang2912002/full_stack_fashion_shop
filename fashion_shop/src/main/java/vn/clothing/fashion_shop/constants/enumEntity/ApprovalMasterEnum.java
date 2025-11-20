package vn.clothing.fashion_shop.constants.enumEntity;
/**
 * Trạng thái phê duyệt cho các entity (product, inventory, ...).
 *
 * Luồng:
    Tạo mới product -> PENDING -> APPROVED (sau đó có thể tạo tồn kho)
    Update sửa thông tin + quantity -> PENDING -> chưa tạo tồn kho -> APPROVED (sau đó có thể tạo tồn kho)
    Update sửa thông tin không có quantity -> APPROVED -> update thành công -> ko thay đổi tồn kho
    Update sửa thông tin có quantity -> APPROVED -> CANCEL_REJECTED (do có tồn kho rồi)
    Update sửa thông tin có quantity -> NEEDS_ADJUSTMENT -> có tồn kho -> cập nhật thông tin tồn kho -> APPROVED
    Yêu cầu hủy product:
    * Nếu không có tồn kho -> CANCEL_REQUESTED -> CANCEL_APPROVED
    * Nếu còn tồn kho -> CANCEL_REQUESTED -> CANCEL_REJECTED (từ chối hủy)
 */
public enum ApprovalMasterEnum {
    PENDING, 
    APPROVED, 
    REJECTED, 
    CANCELLED,
    ADJUSTMENT,           // Đề xuất điều chỉnh
    CANCEL_REQUESTED,    // Yêu cầu hủy sản phẩm (cần kiểm tra tồn kho)
    CANCEL_APPROVED,     // Yêu cầu hủy được phê duyệt (thường khi không có tồn kho)
    CANCEL_REJECTED      // Yêu cầu hủy bị từ chối (ví dụ còn tồn kho, không cho hủy)
}
