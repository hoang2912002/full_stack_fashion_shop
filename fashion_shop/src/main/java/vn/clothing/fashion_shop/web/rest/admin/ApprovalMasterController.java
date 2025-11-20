package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.mapper.ApprovalMasterMapper;
import vn.clothing.fashion_shop.service.ApprovalMasterService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalMasterRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalMasterResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/admin/approvalMasters")
@RequiredArgsConstructor
public class ApprovalMasterController {
    private final ApprovalMasterMapper approvalMasterMapper;
    private final ApprovalMasterService approvalMasterService;
    @PostMapping("")
    public ResponseEntity<ApprovalMasterResponse> createApprovalMaster(
        @RequestBody @Valid ApprovalMasterRequest approvalMasterRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.approvalMasterService.createApprovalMaster(approvalMasterMapper.toValidator(approvalMasterRequest)));
    }
    
    @PutMapping("")
    public ResponseEntity<ApprovalMasterResponse> updateApprovalMaster(
        @RequestBody @Valid ApprovalMasterRequest approvalMasterRequest
    ) {
        return ResponseEntity.ok().body(this.approvalMasterService.updateApprovalMaster(approvalMasterMapper.toValidator(approvalMasterRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalMasterResponse> getApprovalMasterById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(this.approvalMasterService.getApprovalMasterById(id));
    }
    
    @GetMapping("")
    public ResponseEntity<PaginationResponse> getAllApprovalMaster(
        Pageable pageable,
        @Filter Specification spec
    ) {
        return ResponseEntity.ok().body(this.approvalMasterService.getAllApprovalMaster(pageable, spec));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApprovalMasterById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}
