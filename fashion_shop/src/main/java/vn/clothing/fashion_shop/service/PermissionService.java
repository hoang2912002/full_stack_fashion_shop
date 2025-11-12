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
import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.PermissionResponse;

public interface PermissionService {
    List<Permission> getPermissionByListId(List<Long> listId);

    PermissionResponse createPermission(Permission permission);

    PermissionResponse updatePermission(Permission permission);

    PermissionResponse getPermissionById(Long id);

    PaginationResponse getAllPermission(Pageable pageable, Specification specification);

    boolean checkPermissionExist(String apiPath, String method, Long checkId);

    Permission getRawPermissionById(Long id);
}
