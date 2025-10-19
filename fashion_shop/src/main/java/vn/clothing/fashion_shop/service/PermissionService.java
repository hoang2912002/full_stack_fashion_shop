package vn.clothing.fashion_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.repository.PermissionRepository;

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
}
