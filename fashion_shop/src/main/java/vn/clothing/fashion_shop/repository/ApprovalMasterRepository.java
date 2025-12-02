package vn.clothing.fashion_shop.repository;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.constants.enumEntity.ApprovalMasterEnum;
import vn.clothing.fashion_shop.domain.ApprovalMaster;

public interface ApprovalMasterRepository extends JpaRepository<ApprovalMaster, Long>, JpaSpecificationExecutor<ApprovalMaster> {
    Optional<ApprovalMaster> findByEntityTypeAndStatusAndStep(String entityType, ApprovalMasterEnum status, Integer step);
    Optional<ApprovalMaster> findByEntityTypeAndStatus(String entityType, ApprovalMasterEnum status);
    Optional<ApprovalMaster> findByEntityTypeAndStatusAndStepAndIdNot(String entityType, ApprovalMasterEnum status, Integer step, Long id);
    List<ApprovalMaster> findAllByEntityType(String entityType);
}
