package vn.clothing.fashion_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.clothing.fashion_shop.domain.Role;


public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role>{
    
}
