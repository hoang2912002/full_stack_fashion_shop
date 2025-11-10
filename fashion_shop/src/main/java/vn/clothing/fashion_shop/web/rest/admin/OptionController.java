package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Option;
import vn.clothing.fashion_shop.mapper.OptionMapper;
import vn.clothing.fashion_shop.service.OptionService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.requests.OptionRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.OptionResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/admin/options")
@RequiredArgsConstructor
public class OptionController {
    
    private final OptionService optionService;
    private final OptionMapper optionMapper;
    
    @PostMapping("")
    @ApiMessageResponse("Thêm option thành công")
    public ResponseEntity<OptionResponse> createOption(
        @RequestBody @Valid OptionRequest option
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.optionService.createOption(optionMapper.toValidator(option)));
    }

    @PutMapping("")
    @ApiMessageResponse("Cập nhật option thành công")
    public ResponseEntity<OptionResponse> updateOption(
        @RequestBody @Valid OptionRequest option
    ) {        
        return ResponseEntity.ok(this.optionService.updateOption(optionMapper.toValidator(option)));
    }
    
    @GetMapping("/{id}")
    @ApiMessageResponse("Lấy option theo id thành công")
    public ResponseEntity<OptionResponse> getOptionById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(this.optionService.getOptionById(id));
    }
    
    @GetMapping("")
    @ApiMessageResponse("Lấy danh sách option thành công")
    public ResponseEntity<PaginationDTO> getAllOption(
        Pageable pageable,
        @Filter Specification specification
    ) {
        return ResponseEntity.ok(this.optionService.getAllOption(pageable,specification));
    }

    @DeleteMapping("/{id}")
    @ApiMessageResponse("Xóa option theo id thành công")
    public ResponseEntity<Void> deleteOptionById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(null);
    }
}
