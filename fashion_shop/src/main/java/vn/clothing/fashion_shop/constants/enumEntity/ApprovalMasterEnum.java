package vn.clothing.fashion_shop.constants.enumEntity;
/**
 * Trạng thái phê duyệt cho các entity (product, inventory, ...).
 *
 * Luồng:
    Tạo mới product -> PENDING -> APPROVED (sau đó có thể tạo tồn kho)
    Update sửa thông tin + quantity -> PENDING -> chưa tạo tồn kho -> APPROVED (sau đó có thể tạo tồn kho)
    Update sửa thông tin -> APPROVED -> Lỗi -> Yêu cầu điều chỉnh -> NEEDS_ADJUSTMENT
    Update sửa thông tin -> NEEDS_ADJUSTMENT -> Lỗi đợi phê duyệt ADJUSTMENT.
    Update sửa thông tin có quantity -> ADJUSTMENT -> cập nhật thông tin sản phẩm ->FINISHED_ADJUSTMENT -> APPROVED -> Cập nhật tồn kho.
    Update sửa thông tin có quantity -> ADJUSTMENT -> Cập nhật tồn kho -> REJECTED -> Không cập nhật tồn kho.
    Update sửa thông tin có quantity -> REJECTED -> Yêu cầu điều chỉnh -> NEEDS_ADJUSTMENT.
 */
public enum ApprovalMasterEnum {
    PENDING, 
    APPROVED, 
    REJECTED, 
    CANCELLED,
    ADJUSTMENT,           // Chỉnh sửa tồn kho
    NEEDS_ADJUSTMENT,    // Đề xuất điều chỉnh
    FINISHED_ADJUSTMENT, // Hoàn tất điều chỉnh
    // CANCEL_REQUESTED,    // Yêu cầu hủy sản phẩm (cần kiểm tra tồn kho)
    // CANCEL_APPROVED,     // Yêu cầu hủy được phê duyệt (thường khi không có tồn kho)
    // CANCEL_REJECTED      // Yêu cầu hủy bị từ chối (ví dụ còn tồn kho, không cho hủy)
}
