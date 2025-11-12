package vn.clothing.fashion_shop.web.rest.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private InnerMetaPaginationResponse meta;
    private Object data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerMetaPaginationResponse {
        private int page;
        private int pageSize;
        private int pages;
        private Long total;
    }
}
