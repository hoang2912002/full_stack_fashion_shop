package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Manufacture;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/admin/manufactures")
public class ManufactureController {
    
    @PostMapping("")
    @ApiMessageResponse("Thêm mới nhà sản xuất thành công")
    public ResponseEntity<Manufacture> createManufacture(
        @RequestBody Manufacture manufacture
    ) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("")
    @ApiMessageResponse("Cập nhật nhà sản xuất thành công")
    public ResponseEntity<Manufacture> updateManufacture(
        @RequestBody Manufacture manufacture
    ) {
        //TODO: process PUT request
        
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy nhà sản xuất theo id thành công")
    public ResponseEntity<Manufacture> getManufactureById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách nhà sản xuất thành công")
    public ResponseEntity<PaginationResponse> getAllManufactures(
        Pageable pageable,
        @Filter Specification specification
    ) {
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa nhà sản xuất theo id thành công")
    public ResponseEntity<Void> deleteManufactureById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }

}
