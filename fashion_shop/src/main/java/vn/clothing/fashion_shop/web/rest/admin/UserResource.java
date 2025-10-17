package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/")
public class UserResource {
    @GetMapping("/")
    public String getMethodName() {
        return "Hello";
    }
    
}
