package vn.clothing.fashion_shop.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;

public interface RoleService {

    Role handleGetRoleById(Long id);

    RoleResponse createRole(Role role);

    RoleResponse updateRole(Role role);

    RoleResponse getRoleById(Long id);

    PaginationDTO getAllRole(Pageable pageable, Specification spec);

    boolean existsBySlug(String slug, Long checkId);

    Role getRawRoleById(Long id);

    void deleteRoleById(Long id);
}
