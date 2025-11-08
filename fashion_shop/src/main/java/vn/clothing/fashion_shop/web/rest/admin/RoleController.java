package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.mapper.RoleMapper;
import vn.clothing.fashion_shop.service.RoleService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.RoleRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;




@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PostMapping("")
    @ApiMessageResponse("Thêm mới role thành công")
    public ResponseEntity<RoleResponse> createRole(
        @RequestBody @Valid RoleRequest role
    ) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(roleMapper.toRequest(role)));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật role thành công")
    public ResponseEntity<RoleResponse> updateRole(
        @RequestBody @Valid RoleRequest role
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.updateRole(roleMapper.toRequest(role)));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy vai trò theo id thành công")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoleById(id));
    }

    @GetMapping("")
    public ResponseEntity<PaginationDTO> getAllRole(
        Pageable pageable,
        @Filter Specification<Role> spec
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getAllRole(pageable,spec));
    }
    
    
}
