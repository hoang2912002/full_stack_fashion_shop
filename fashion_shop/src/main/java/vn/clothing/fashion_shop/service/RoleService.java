package vn.clothing.fashion_shop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    @Transactional(readOnly = true)
    public Role handleGetRoleById(Long id){
        Optional<Role> role = this.roleRepository.findById(id);
        return role.isPresent() && role != null ? role.get() : null;
    }
}
