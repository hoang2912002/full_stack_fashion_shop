package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.ApprovalMaster;
import vn.clothing.fashion_shop.web.rest.DTO.requests.ApprovalMasterRequest;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalMasterResponse;
import vn.clothing.fashion_shop.web.rest.DTO.responses.ApprovalMasterResponse.InnerApprovalMasterResponse;

@Mapper(
    componentModel = "spring",
    uses = {
        RoleMapper.class,
        UserMapper.class
    }
)
public interface ApprovalMasterMapper extends EntityMapper<ApprovalMasterResponse, ApprovalMaster> {
    ApprovalMasterMapper INSTANCE = Mappers.getMapper(ApprovalMasterMapper.class);

    @Named("toMiniDto")
    InnerApprovalMasterResponse toMiniDto(ApprovalMaster approvalMaster);

    @Named("toDto")
    ApprovalMasterResponse toDto(ApprovalMaster approvalMaster);
    List<ApprovalMasterResponse> toDto(List<ApprovalMaster> approvalMasters);

    ApprovalMaster toEntity(ApprovalMasterResponse dto);
    ApprovalMaster toValidator(ApprovalMasterRequest dto);
}
