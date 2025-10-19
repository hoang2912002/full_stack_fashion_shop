package vn.clothing.fashion_shop.web.rest.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO {
    private InnerMetaPaginationDTO meta;
    private Object data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerMetaPaginationDTO {
        private int page;
        private int pageSize;
        private int pages;
        private Long total;
    }
}
