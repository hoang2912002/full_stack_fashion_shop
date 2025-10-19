package vn.clothing.fashion_shop.constants.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.web.rest.DTO.PaginationDTO;
public class ConvertPagination {
    public static <E, D> PaginationDTO handleConvert(Pageable pageable, Page<E> data, List<D> listData){
        PaginationDTO rs = new PaginationDTO();
        PaginationDTO.InnerMetaPaginationDTO meta = new PaginationDTO.InnerMetaPaginationDTO(1,10,1,0L);
        
        if (data == null) {
            rs.setMeta(meta);
            rs.setData(null);
            return rs;
        }

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(data.getTotalPages());
        meta.setTotal(data.getTotalElements());

        rs.setMeta(meta);
        rs.setData(listData);
        return rs;
    }
}
