package vn.clothing.fashion_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Manufacture;


public interface ManufactureRepository extends JpaRepository<Manufacture, Long>, JpaSpecificationExecutor<Manufacture>  {
    
}
