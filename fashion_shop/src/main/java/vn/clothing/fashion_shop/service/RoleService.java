package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.RoleRepository;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.CreateRoleDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.GetRoleDTO;
import vn.clothing.fashion_shop.web.rest.DTO.role.UpdateRoleDTO;
import vn.clothing.fashion_shop.web.rest.DTO.user.GetUserDTO;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    @Transactional(readOnly = true)
    public Role handleGetRoleById(Long id){
        Optional<Role> role = this.roleRepository.findById(id);
        return role.isPresent() && role != null ? role.get() : null;
    }

    @Transactional()
    public CreateRoleDTO createRole(Role role){
        String slug = SlugUtil.toSlug(role.getName());
        if(existsBySlug(slug, null)){
            throw new RuntimeException("Vai trò với tên: " + role.getName() + " đã tồn tại");
        }
        List<Permission> permissions = null;
        if(role.getPermissions() != null && role.getPermissions().size() > 0){
            List<Long> pIdList = role.getPermissions().stream().map(
                p -> p.getId()
            ).toList();
            permissions = this.permissionService.getPermissionByListId(pIdList);
        }
        Role createRole = new Role();
        BeanUtils.copyProperties(role, createRole);
        createRole.setPermissions(permissions);
        createRole.setSlug(slug);
        createRole = this.roleRepository.save(createRole);
        CreateRoleDTO createRoleDTO = new CreateRoleDTO();
        BeanUtils.copyProperties(createRole, createRoleDTO);
        return createRoleDTO;
    }

    @Transactional()
    public UpdateRoleDTO updateRole(Role role){
        String slug = SlugUtil.toSlug(role.getName());
        Role updateRole = getRawRoleById(role.getId());
        if(updateRole == null){
            throw new RuntimeException("Vai trò với id: " + role.getId() + " không tồn tại");
        }
        if(existsBySlug(slug, role.getId())){
            throw new RuntimeException("Vai trò với tên: " + role.getName() + " đã tồn tại");
        }
        List<Permission> permissions = null;
        if(role.getPermissions() != null && role.getPermissions().size() > 0){
            List<Long> pIdList = role.getPermissions().stream().map(
                p -> p.getId()
            ).toList();
            permissions = this.permissionService.getPermissionByListId(pIdList);
        }
        updateRole.setName(role.getName());
        updateRole.setSlug(slug);
        updateRole.setPermissions(permissions);
        updateRole = this.roleRepository.save(updateRole);
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        BeanUtils.copyProperties(updateRole, updateRoleDTO);
        return updateRoleDTO;
    }

    @Transactional()
    public GetRoleDTO getRoleById(Long id){
        Role role = getRawRoleById(id);
        if(role == null){
            throw new RuntimeException("Vai trò với id: " + role.getId() + " không tồn tại");
        }
        GetRoleDTO getRoleDTO = new GetRoleDTO();
        List<GetRoleDTO.InnerPermissionRoleDTO> listPermissionRoleDTOs = role.getPermissions().size() > 0 ? role.getPermissions().stream().map(
            p -> {
                GetRoleDTO.InnerPermissionRoleDTO getPermissionDTO = new GetRoleDTO.InnerPermissionRoleDTO();
                BeanUtils.copyProperties(p, getPermissionDTO);
                return getPermissionDTO;
            }
        ).toList() : null;
        BeanUtils.copyProperties(role, getRoleDTO);
        getRoleDTO.setPermissions(listPermissionRoleDTOs);
        return getRoleDTO;
    }

    @Transactional()
    public PaginationDTO getAllRole(Pageable pageable, Specification spec){
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);
        List<GetRoleDTO> roleDTOs = roles.getContent().stream().map(r -> {
            GetRoleDTO roleDTO = new GetRoleDTO();
            BeanUtils.copyProperties(r, roleDTO);

            // if (r.getPermissions() != null) {
            //     GetRoleDTO.InnerPermissionRoleDTO roleDTO = new  GetRoleDTO.InnerPermissionRoleDTO();
            //     BeanUtils.copyProperties(r.getPermissions(), roleDTO);
            //     roleDTO.setPermissions();
            // }

            return roleDTO;
        }).toList();

        return ConvertPagination.handleConvert(pageable, roles, roleDTOs);
    }

    public boolean existsBySlug(String slug, Long checkId){
        return checkId != null ? this.roleRepository.existsBySlugAndIdNot(slug, checkId) : this.roleRepository.existsBySlug(slug);
    }

    public Role getRawRoleById(Long id){
        Optional<Role> role = this.roleRepository.findById(id);
        return role.isPresent() && role.get() != null ? role.get() : null;
    }
}
