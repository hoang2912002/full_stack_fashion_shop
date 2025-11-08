package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.service.PermissionService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PermissionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/admin/permissions")
public class PermissionController {
    
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm mới permission thành công")
    public ResponseEntity<PermissionResponse> createPermission(
        @RequestBody @Valid PermissionRequest permission
    ) {
        Permission createPermission = new Permission();
        BeanUtils.copyProperties(permission, createPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(createPermission));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật permission thành công")
    public ResponseEntity<PermissionResponse> updatePermission(
        @RequestBody @Valid PermissionRequest permission
    ) {
        Permission updatePermission = new Permission();
        BeanUtils.copyProperties(permission, updatePermission);
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.updatePermission(updatePermission));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy permission theo id thành công")
    public ResponseEntity<PermissionResponse> getPermissionById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.permissionService.getPermissionById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách permission thành công")
    public ResponseEntity<PaginationDTO> getAllPermission(
        Pageable pageable,
        @Filter Specification<Permission> spec
    ) {
        return ResponseEntity.ok(this.permissionService.getAllPermission(pageable,spec));
    }
    
    @DeleteMapping("/{id}")
    public void deletePermissionById(
        @PathVariable("id") Long id
    ){
        
    }
    
}
