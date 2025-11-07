package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.mapper.UserMapper;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.UserRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.UserResponse;



@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("")
    @ApiMessageResponse("Tạo mới người dùng thành công")
    public ResponseEntity<UserResponse> createUser(
        @RequestBody @Valid UserRequest user
    ) throws Exception {
        User createUser = userMapper.toRequest(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(createUser));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật người dùng thành công")
    public ResponseEntity<UserResponse> updateUser(
        @RequestBody @Valid UserRequest user
    ) {
        User updateUser = userMapper.toRequest(user);
        return ResponseEntity.ok(this.userService.updateUser(updateUser));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy user theo id thành công")
    public ResponseEntity<UserResponse> getUserById(
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
