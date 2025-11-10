package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.mapper.OptionValueMapper;
import vn.clothing.fashion_shop.service.OptionValueService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OptionValueRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionValueResponse;

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

@RestController
@RequestMapping("/api/v1/admin/optionValues")
@RequiredArgsConstructor
public class OptionValueController {
    private final OptionValueService optionValueService;
    private final OptionValueMapper optionValueMapper;

    @PostMapping("")
    @ApiMessageResponse("Thêm option value thành công")
    public ResponseEntity<OptionValueResponse> createOptionValue(
        @RequestBody @Valid OptionValueRequest optionValue
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.optionValueService.createOptionValue(optionValueMapper.toValidator(optionValue)));
    }
    
    @PutMapping("")
    @ApiMessageResponse("Cập nhật option value thành công")
    public ResponseEntity<OptionValueResponse> updateOptionValue(
        @RequestBody @Valid OptionValueRequest optionValue
    ) {        
        return ResponseEntity.ok(this.optionValueService.updateOptionValue(optionValueMapper.toValidator(optionValue)));
    }

    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy option value theo id thành công")
    public ResponseEntity<OptionValueResponse> getOptionValueById(
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
