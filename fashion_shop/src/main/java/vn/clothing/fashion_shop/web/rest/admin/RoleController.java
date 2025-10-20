package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.service.RoleService;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.CreateRoleDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.GetRoleDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.UpdateRoleDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/admin/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm mới role thành công")
    public ResponseEntity<CreateRoleDTO> createRole(
        @RequestBody Role role
    ) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật role thành công")
    public ResponseEntity<UpdateRoleDTO> updateRole(
        @RequestBody Role role
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.updateRole(role));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy vai trò theo id thành công")
    public ResponseEntity<GetRoleDTO> getRoleById(@PathVariable("id") Long id) {
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
