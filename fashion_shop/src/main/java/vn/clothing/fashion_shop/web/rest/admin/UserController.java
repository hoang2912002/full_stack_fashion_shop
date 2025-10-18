package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.constants.annotation.SkipWrapResponse;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.DTO.user.CreateUserDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        @RequestBody User user
    ) throws Exception {
        CreateUserDTO userCreateDTO = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreateDTO);
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật người dùng thành công")
    public ResponseEntity<User> updateUser(
        @RequestBody User user
    ) {

        return ResponseEntity.ok(null);
    }
}
