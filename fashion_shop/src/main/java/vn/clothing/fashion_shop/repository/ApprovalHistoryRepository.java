package vn.clothing.fashion_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import vn.clothing.fashion_shop.domain.ApprovalHistory;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long>, JpaSpecificationExecutor<ApprovalHistory>{
    List<ApprovalHistory> findAllByApprovalMasterIdOrderByApprovedAtAsc(Long approvalMasterId);
    List<ApprovalHistory> findAllByRequestIdOrderByApprovedAtAsc(Long requestId);
    List<ApprovalHistory> findAllByApprovalMasterIdInAndRequestId(List<Long> approvalMasterIds, Long requestId);
    ApprovalHistory findFirstByApprovalMasterIdInAndRequestIdOrderByApprovedAtDesc(List<Long> approvalMasterIds, Long requestId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM ApprovalHistory a WHERE a.id = :id")
    @QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "0") // 0 = fail immediately
    })
    /**
     * @QueryHints là công cụ bổ sung cho query trong JPA, 
     * cho phép bạn truyền các “tùy chọn nâng cao” mà JPQL/HQL hay SQL bình thường không làm được hoặc không có cú pháp trực tiếp.
     * 
     *  | Trường hợp                    | Ví dụ                                                                                                                                                                           |
        | ----------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
        | **Pessimistic Lock timeout**  | Khi dùng `@Lock(LockModeType.PESSIMISTIC_WRITE)` mà muốn query **fail ngay** nếu row đang bị khóa → `@QueryHints(@QueryHint(name="javax.persistence.lock.timeout", value="0"))` |
        | **Caching**                   | Hibernate second-level cache, query cache: `@QueryHint(name="org.hibernate.cacheable", value="true")`                                                                           |
        | **Fetch size / read-only**    | Cấu hình fetch size: `@QueryHint(name="org.hibernate.fetchSize", value="50")`<br>Read-only query: `@QueryHint(name="org.hibernate.readOnly", value="true")`                     |
        | **Timeout query**             | Đặt timeout tính bằng millisecond: `@QueryHint(name="javax.persistence.query.timeout", value="1000")`                                                                           |
        | **Custom hints của provider** | Mỗi JPA provider (Hibernate, EclipseLink) có thể hỗ trợ các hint riêng, ví dụ: batch size, comment SQL…                                                                         |
     * 
     * javax.persistence.lock.timeout chỉ hiệu lực khi bạn dùng LockModeType.PESSIMISTIC_READ hoặc PESSIMISTIC_WRITE.
     */
    //lock.timeout=0 → nếu row đang bị lock, Hibernate throw PessimisticLockException ngay lập tức, không chờ.
    ApprovalHistory lockApprovalHistoryById(@Param("id") Long id);
}
