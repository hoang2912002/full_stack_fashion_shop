package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.service.PermissionService;
import vn.clothing.fashion_shop.web.rest.DTO.requests.PermissionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @ApiMessageResponse("permission.success.create")
    public ResponseEntity<PermissionResponse> createPermission(
        @RequestBody @Valid PermissionRequest permission
    ) {
        Permission createPermission = new Permission();
        BeanUtils.copyProperties(permission, createPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(createPermission));
    }
    
    @PutMapping("")
    @ApiMessageResponse("permission.success.update")
    public ResponseEntity<PermissionResponse> updatePermission(
        @RequestBody @Valid PermissionRequest permission
    ) {
        Permission updatePermission = new Permission();
        BeanUtils.copyProperties(permission, updatePermission);
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.updatePermission(updatePermission));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("permission.success.get.single")
    public ResponseEntity<PermissionResponse> getPermissionById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.permissionService.getPermissionById(id));
    }

    @GetMapping("")
    @ApiMessageResponse("permission.success.get.all")
    public ResponseEntity<PaginationResponse> getAllPermission(
        Pageable pageable,
        @Filter Specification<Permission> spec
    ) {
        return ResponseEntity.ok(this.permissionService.getAllPermission(pageable,spec));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("permission.success.delete")
    public void deletePermissionById(
        @PathVariable("id") Long id
    ){
        
    }
    
}
