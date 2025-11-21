package vn.clothing.fashion_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.ApprovalHistory;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long>, JpaSpecificationExecutor<ApprovalHistory>{
    List<ApprovalHistory> findAllByApprovalMasterIdOrderByApprovedAtAsc(Long approvalMasterId);
    List<ApprovalHistory> findAllByApprovalMasterIdInAndRequestId(List<Long> approvalMasterIds, Long requestId);
    ApprovalHistory findFirstByApprovalMasterIdInAndRequestIdOrderByApprovedAtDesc(List<Long> approvalMasterIds, Long requestId);
}
