package vn.clothing.fashion_shop.web.rest.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.annotation.ApiMessageResponse;
import vn.clothing.fashion_shop.domain.Inventory;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/inventories")
public class InventoryController {
    
    @PostMapping("")
    @ApiMessageResponse("inventory.success.create")
    public ResponseEntity<Inventory> createInventory(
        @RequestBody Inventory inventory
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("")
    @ApiMessageResponse("inventory.success.update")
    public ResponseEntity<Inventory> updateInventory(
        @RequestBody Inventory inventory    
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    @GetMapping("/{id}")
    @ApiMessageResponse("inventory.success.get.single")
    public ResponseEntity<Inventory> getInventoryById(
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    @GetMapping("")
    @ApiMessageResponse("inventory.success.get.all")
    public ResponseEntity<PaginationResponse> getAllInventories(
        Pageable pageable,
        @Filter Specification specification
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    @ApiMessageResponse("inventory.success.delete")
    public void deleteInventoryById(
        @PathVariable("id") Long id
    ){

    }
}
