package vn.clothing.fashion_shop.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.mapper.RoleMapper;
import vn.clothing.fashion_shop.repository.RoleRepository;
import vn.clothing.fashion_shop.service.PermissionService;
import vn.clothing.fashion_shop.service.RoleService;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.responses.RoleResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public Role handleGetRoleById(Long id){
        try {
            final Optional<Role> role = this.roleRepository.findById(id);
            return role.isPresent() ? role.get() : null;
        } catch (Exception e) {
            log.error("[getRawAddressById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RoleResponse createRole(Role role){
        log.info("[createRole] start create role ....");
        try {
            final String slug = SlugUtil.toSlug(role.getName());
            if(existsBySlug(slug, null)){
                throw new ServiceException(EnumError.ROLE_DATA_EXISTED_NAME, "role.exist.name",Map.of("name", role.getName()));
            }
            List<Permission> permissions = new ArrayList<>();
            if(role.getPermissions() != null && !role.getPermissions().isEmpty()){
                List<Long> pIdList = role.getPermissions().stream().map(
                    p -> p.getId()
                ).toList();
                permissions = this.permissionService.getPermissionByListId(pIdList);
            }
            final Role createRole = Role.builder()
            .name(role.getName())
            .slug(slug)
            .activated(true)
            .permissions(permissions)
            .build();
            return roleMapper.toDto(this.roleRepository.save(createRole));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[createRole] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RoleResponse updateRole(Role role){
        log.info("[updateRole] update create role ....");
        try {
            final String slug = SlugUtil.toSlug(role.getName());
            final Role updateRole = getRawRoleById(role.getId());
            if(updateRole == null){
                throw new ServiceException(EnumError.ROLE_DATA_EXISTED_NAME, "role.not.found.id",Map.of("id", role.getId()));
            }
            if(existsBySlug(slug, role.getId())){
                throw new ServiceException(EnumError.ROLE_DATA_EXISTED_NAME, "role.exist.name",Map.of("name", role.getName()));
            }
            List<Permission> permissions = new ArrayList<>();
            if(role.getPermissions() != null && !role.getPermissions().isEmpty()){
                List<Long> pIdList = role.getPermissions().stream().map(
                    p -> p.getId()
                ).toList();
                permissions = this.permissionService.getPermissionByListId(pIdList);
            }
            updateRole.setName(role.getName());
            updateRole.setSlug(slug);
            updateRole.setPermissions(permissions);
            return roleMapper.toDto(this.roleRepository.save(updateRole));
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[updateRole] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly=true)
    public RoleResponse getRoleById(Long id){
        try {
            final Role role = getRawRoleById(id);
            if(role == null){
                throw new ServiceException(EnumError.ROLE_DATA_EXISTED_NAME, "role.not.found.id",Map.of("id", id));
            }
            return roleMapper.toDto(role);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[getRoleById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly=true)
    public PaginationDTO getAllRole(Pageable pageable, Specification spec){
        try {
            Page<Role> roles = this.roleRepository.findAll(spec, pageable);
            List<RoleResponse> roleDTOs = roles.getContent().stream().map(r -> {
                return roleMapper.toDtoWithoutPermission(r);
            }).toList();
            return ConvertPagination.handleConvert(pageable, roles, roleDTOs);
        } catch (Exception e) {
            log.error("[getAllRole] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly=true)
    public boolean existsBySlug(String slug, Long checkId){
        try {
            return checkId != null ? this.roleRepository.existsBySlugAndIdNot(slug, checkId) : this.roleRepository.existsBySlug(slug);
        } catch (Exception e) {
            log.error("[existsBySlug] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly=true)
    public Role getRawRoleById(Long id){
        try {
            Optional<Role> role = this.roleRepository.findById(id);
            return role.isPresent() && role.get() != null ? role.get() : null;
        } catch (Exception e) {
            log.error("[getRawRoleById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public void deleteRoleById(Long id){
        
    }
}
