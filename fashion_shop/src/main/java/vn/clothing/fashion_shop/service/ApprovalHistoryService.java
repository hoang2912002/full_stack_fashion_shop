package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.ApprovalHistory;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalHistoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

public interface ApprovalHistoryService {
    ApprovalHistoryResponse createApprovalHistory(ApprovalHistory approvalHistory, boolean skipCheckPeriodDataExist);
    ApprovalHistoryResponse updateApprovalHistory(ApprovalHistory approvalHistory);
    ApprovalHistoryResponse getApprovalHistoryById(Long id);
    PaginationResponse getAllApprovalHistories(Pageable pageable, Specification spec);
    void deleteApprovalHistory(Long id);
    ApprovalHistory findRawApprovalHistoryById(Long id);
    List<ApprovalHistory> findRawAllApprovalHistoryByApprovalMasterId(Long approvalMasterId);
    List<ApprovalHistory> findRawAllApprovalHistoryByApprovalMasterIdsAndRequestId(List<Long> approvalMasterIds, Long requestId, boolean lastApprovalHistoryOnly);
    void handleApprovalHistoryUpSertProduct(
        Product product, Long productId // Đây là id để kiểm tra tạo mới hay cập nhật
    );
}
