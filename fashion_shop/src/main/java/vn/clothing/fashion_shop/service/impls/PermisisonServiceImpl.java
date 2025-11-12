package vn.clothing.fashion_shop.service.impls;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.constants.util.ConvertPagination;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.mapper.PermissionMapper;
import vn.clothing.fashion_shop.repository.PermissionRepository;
import vn.clothing.fashion_shop.service.PermissionService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;
@Service
@RequiredArgsConstructor
@Slf4j
public class PermisisonServiceImpl implements PermissionService{
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionByListId(List<Long> listId){
        List<Permission> lPermissions = this.permissionRepository.findAllByIdIn(listId);
        return lPermissions.size() > 0 ? lPermissions : null;
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public PermissionResponse createPermission(Permission permission){
        log.info("[createPermission] start create permission ....");
        try {
            final String method = permission.getMethod().toUpperCase();
            final String module = permission.getModule().toUpperCase();
            final String apiPath = permission.getApiPath();
            if(checkPermissionExist(apiPath, method, null)){
                throw new ServiceException(
                    EnumError.PERMISSION_DATA_EXISTED_APIPATH, 
                    "permission.exist.apipath.method",
                    Map.of("apiPath", apiPath,"method", method));
            }
            Permission createPermission = Permission.builder()
            .name(permission.getName())
            .apiPath(apiPath)
            .method(method)
            .module(module)
            .activated(true)
            .build();
            return permissionMapper.toDto(this.permissionRepository.saveAndFlush(createPermission)); 
        }
          catch (ServiceException e) {
           throw e;
        } catch (Exception e) {
            log.error("[createPermission] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public PermissionResponse updatePermission(Permission permission){
        log.info("[updatePermission] start create permission ....");
        try {
            final Permission updatePermission = getRawPermissionById(permission.getId());
            final String method = permission.getMethod().toUpperCase();
            final String module = permission.getModule().toUpperCase();
            final String apiPath = permission.getApiPath();
            if(updatePermission == null){
                throw new ServiceException(
                    EnumError.PERMISSION_ERR_NOT_FOUND_ID, 
                    "permission.not.found.id",
                    Map.of("id", permission.getId()));
            }
            if(checkPermissionExist(apiPath, method, permission.getId())){
                throw new ServiceException(
                    EnumError.PERMISSION_DATA_EXISTED_APIPATH, 
                    "permission.exist.apipath.method",
                    Map.of("apiPath", apiPath,"method", method));
            }
            updatePermission.setName(permission.getName());
            updatePermission.setApiPath(apiPath);
            updatePermission.setMethod(method);
            updatePermission.setModule(module);
            updatePermission.setActivated(true);
            return permissionMapper.toDto(updatePermission);
        } catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("[updatePermission] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(Long id){
        try {
            Permission permission = getRawPermissionById(id);
            if(permission == null){
                throw new ServiceException(
                    EnumError.PERMISSION_ERR_NOT_FOUND_ID, 
                    "permission.not.found.id",
                    Map.of("id", id));
            }
            return permissionMapper.toDto(permission);
        } catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("[getPermissionById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse getAllPermission(Pageable pageable, Specification specification){
        try {
            Page<Permission> permissions = this.permissionRepository.findAll(specification, pageable);

            List<PermissionResponse> permissionResDTOs = permissions.getContent().stream().map(permissionMapper::toDto).toList();

            return ConvertPagination.handleConvert(pageable, permissions, permissionResDTOs);
        } catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("[getAllPermission] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public boolean checkPermissionExist(String apiPath, String method, Long checkId){
        return checkId != null ? this.permissionRepository.existsByApiPathAndMethodAndIdNot(apiPath,method,checkId) : this.permissionRepository.existsByApiPathAndMethod(apiPath,method);
    }

    @Override
    @Transactional(readOnly = true)
    public Permission getRawPermissionById(Long id){
        try {
            final Optional<Permission> permission = this.permissionRepository.findById(id);
            return permission.isPresent() ? permission.get() : null;
        } catch (Exception e) {
            log.error("[getRawPermissionById] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }
}
