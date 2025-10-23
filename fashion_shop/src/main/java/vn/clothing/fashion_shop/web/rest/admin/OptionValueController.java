package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.service.OptionValueService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.CreateOptionValueDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.GetOptionValueDTO;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.UpdateOptionValueDTO;

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
@RequestMapping("/api/v1/admin/optionValues")
public class OptionValueController {
    private final OptionValueService optionValueService;

    public OptionValueController(OptionValueService optionValueService) {
        this.optionValueService = optionValueService;
    }

    @PostMapping("")
    @ApiMessageResponse("Thêm option value thành công")
    public ResponseEntity<CreateOptionValueDTO> createOptionValue(
        @RequestBody OptionValue optionValue
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.optionValueService.createOptionValue(optionValue));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật option value thành công")
    public ResponseEntity<UpdateOptionValueDTO> updateOptionValue(
        @RequestBody OptionValue optionValue
    ) {        
        return ResponseEntity.ok(this.optionValueService.updateOptionValue(optionValue));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy option value theo id thành công")
    public ResponseEntity<GetOptionValueDTO> getOptionValueById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.optionValueService.getOptionValueById(id));
    }
    
    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách option value thành công")
    public ResponseEntity<PaginationDTO> getAllOptionValue(
        Pageable pageable,
        @Filter Specification specification
    ) {
        return ResponseEntity.ok(this.optionValueService.getAllOptionValue(pageable,specification));
    }

    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa option value theo id thành công")
    public ResponseEntity<Void> deleteOptionValueById(
        @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(null);
    }
}
