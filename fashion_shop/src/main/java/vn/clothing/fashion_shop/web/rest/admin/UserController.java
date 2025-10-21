package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.constants.annotation.SkipWrapResponse;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.CreateUserDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.GetUserDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.UpdateUserDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.ValidationUserDTO;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @ApiMessageResponse("Tạo mới người dùng thành công")
    public ResponseEntity<CreateUserDTO> createUser(
        @RequestBody @Valid ValidationUserDTO user
    ) throws Exception {
        User createUser = new User();
        BeanUtils.copyProperties(user, createUser);
        CreateUserDTO userCreateDTO = this.userService.createUser(createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreateDTO);
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật người dùng thành công")
    public ResponseEntity<UpdateUserDTO> updateUser(
        @RequestBody @Valid ValidationUserDTO user
    ) {
        User updateUser = new User();
        BeanUtils.copyProperties(user, updateUser);
        UpdateUserDTO updateUserDTO = this.userService.updateUser(updateUser);
        return ResponseEntity.ok(updateUserDTO);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy user theo id thành công")
    public ResponseEntity<GetUserDTO> getUserById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }
    
    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách người dùng thành công")
    public ResponseEntity<PaginationDTO> getAllUser(
        Pageable pageable,
        @Filter Specification<User> spec
    ){
        return ResponseEntity.ok(this.userService.getAllUser(pageable,spec));
    }

    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa người dùng thành công")
    public ResponseEntity<Void> deleteUserById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(this.userService.deleteUserById(id));
    }
}
