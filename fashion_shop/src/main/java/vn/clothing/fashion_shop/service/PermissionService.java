package vn.clothing.fashion_shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.repository.PermissionRepository;
import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
import vn.clothing.fashion_shop.web.rest.DTO.permission.CreatePermissionDTO;
import vn.clothing.fashion_shop.web.rest.DTO.permission.GetPermissionResDTO;
import vn.clothing.fashion_shop.web.rest.DTO.permission.UpdatePermissionDTO;

@Service
public class PermissionService {
    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getPermissionByListId(List<Long> listId){
        List<Permission> lPermissions = this.permissionRepository.findAllByIdIn(listId);
        return lPermissions.size() > 0 ? lPermissions : null;
    }

    public CreatePermissionDTO createPermission(Permission permission){
        String method = permission.getMethod().toUpperCase();
        String module = permission.getModule().toUpperCase();
        if(checkPermissionExist(permission.getApiPath(), method, null)){
            throw new RuntimeException("Quyền hạn của chức năng này đã tồn tại");
        }
        Permission createPermission = new Permission();
        BeanUtils.copyProperties(permission, createPermission);
        createPermission.setMethod(method);
        createPermission.setModule(module);
        createPermission.setActivated(true);
        createPermission = this.permissionRepository.saveAndFlush(createPermission);
        CreatePermissionDTO permissionDTO = new CreatePermissionDTO();
        BeanUtils.copyProperties(createPermission, permissionDTO);
        return permissionDTO;
    }

    public UpdatePermissionDTO updatePermission(Permission permission){
        Permission updatePermission = getRawPermissionById(permission.getId());
        if(updatePermission == null){
            throw new RuntimeException("Quyền hạng với id: " + permission.getId() + " không tồn tại");
        }
        updatePermission.setName(permission.getName());
        updatePermission = this.permissionRepository.saveAndFlush(updatePermission);

        UpdatePermissionDTO updatePermissionDTO = new UpdatePermissionDTO();
        BeanUtils.copyProperties(updatePermission, updatePermissionDTO);
        return updatePermissionDTO;
    }

    public GetPermissionResDTO getPermissionById(Long id){
        Permission permission = getRawPermissionById(id);
        if(permission == null){
            throw new RuntimeException("Quyền hạng với id: " + id + " không tồn tại");
        }
        GetPermissionResDTO getPermissionResDTO = new GetPermissionResDTO();
        BeanUtils.copyProperties(permission, getPermissionResDTO);
        return getPermissionResDTO;
    }

    public PaginationDTO getAllPermission(Pageable pageable, Specification specification){
        Page<Permission> permissions = this.permissionRepository.findAll(specification, pageable);

        List<GetPermissionResDTO> permissionResDTOs = permissions.getContent().stream().map(r -> {
            GetPermissionResDTO permissionResDTO = new GetPermissionResDTO();
            BeanUtils.copyProperties(r, permissionResDTO);
            return permissionResDTO;
        }).toList();

        return ConvertPagination.handleConvert(pageable, permissions, permissionResDTOs);
    }

    public boolean checkPermissionExist(String apiPath, String method, Long checkId){
        return checkId != null ? this.permissionRepository.existsByApiPathAndMethodAndIdNot(apiPath,method,checkId) : this.permissionRepository.existsByApiPathAndMethod(apiPath,method);
    }

    public Permission getRawPermissionById(Long id){
        if(id == null){
            throw new RuntimeException("Id không được để trống");
        }
        Optional<Permission> permission = this.permissionRepository.findById(id);
        return permission.isPresent() ? permission.get() : null;
    }
}
