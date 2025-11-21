package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.ApprovalHistory;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalHistoryRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalHistoryResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalHistoryResponse.InnerApprovalHistoryResponse;

@Mapper(
    componentModel = "spring",
    uses = {
        ApprovalMasterMapper.class,
    }
)
public interface ApprovalHistoryMapper extends EntityMapper<ApprovalHistoryResponse, ApprovalHistory> {
    ApprovalHistoryMapper INSTANCE = Mappers.getMapper(ApprovalHistoryMapper.class);

    @Named("toMiniDto")
    InnerApprovalHistoryResponse toMiniDto(ApprovalHistory approvalHistory);

    @Named("toDto")
    ApprovalHistoryResponse toDto(ApprovalHistory approvalHistory);
    List<ApprovalHistoryResponse> toDto(List<ApprovalHistory> approvalHistories);

    ApprovalHistory toEntity(ApprovalHistoryResponse dto);
    ApprovalHistory toValidator(ApprovalHistoryRequest dto);
}
