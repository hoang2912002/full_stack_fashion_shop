package vn.clothing.fashion_shop.constants.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.clothing.fashion_shop.web.rest.DTO.responses.PaginationResponse;
public class ConvertPagination {
    public static <E, D> PaginationResponse handleConvert(Pageable pageable, Page<E> data, List<D> listData){
        PaginationResponse rs = new PaginationResponse();
        PaginationResponse.InnerMetaPaginationResponse meta = new PaginationResponse.InnerMetaPaginationResponse(1,10,1,0L);
        
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
