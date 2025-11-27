package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.mapper.ApprovalHistoryMapper;
import vn.clothing.fashion_shop.service.ApprovalHistoryService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalHistoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalMasterRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalHistoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/admin/approvalHistories")
@RequiredArgsConstructor
public class ApprovalHistoryController {
    private final ApprovalHistoryMapper approvalHistoryMapper;
    protected final ApprovalHistoryService approvalHistoryService;

    @PostMapping("")
    @ApiMessageResponse("approval.history.success.create")
    public ResponseEntity<ApprovalHistoryResponse> createApprovalHistory(
        @RequestBody @Valid ApprovalHistoryRequest approvalHistoryRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.approvalHistoryService.createApprovalHistory(
            approvalHistoryMapper.toValidator(approvalHistoryRequest), 
            false, 
            approvalHistoryRequest.getEntityType()
        ));
    }
    
    @PutMapping("")
    @ApiMessageResponse("approval.history.success.update")
    public ResponseEntity<ApprovalHistoryResponse> updateApprovalHistory(
        @RequestBody @Valid ApprovalHistoryRequest approvalHistoryRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.approvalHistoryService.updateApprovalHistory(
            approvalHistoryMapper.toValidator(approvalHistoryRequest), 
            false, 
            approvalHistoryRequest.getEntityType()
        ));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("approval.history.success.get.single")
    public ResponseEntity<ApprovalHistoryResponse> getApprovalHistoryById(
        @PathVariable("id") Long id    
    ) {
        return ResponseEntity.ok(this.approvalHistoryService.getApprovalHistoryById(id));
    }
    
    @GetMapping("")
    @ApiMessageResponse("approval.history.success.get.all")
    public ResponseEntity<PaginationResponse> getAllApprovalHistory(
        Pageable pageable,
        @Filter Specification spec
    ) {
        return ResponseEntity.ok().body(this.approvalHistoryService.getAllApprovalHistories(pageable, spec));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApprovalHistoryById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}
