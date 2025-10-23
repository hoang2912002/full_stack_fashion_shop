package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import vn.clothing.fashion_shop.domain.OptionValue;
import vn.clothing.fashion_shop.domain.Product;
import vn.clothing.fashion_shop.web.rest.DTO.optionValue.GetOptionValueDTO;

//componentModel = "spring" giúp Spring tự inject mapper qua @Autowired.
/*
 * 🧩 1️⃣ MapStruct thật ra sinh ra class thật ở compile-time
 *      - MapStruct không thực thi mapping bằng reflection như BeanUtils, mà nó sinh code Java thật trong quá trình biên dịch (compile-time).
 *      - Khi build project, MapStruct sẽ generate một class thật (ở thư mục build/generated/sources/annotationProcessor/...)
 *      
 * ⚙️ 2️⃣ componentModel = "spring" giúp Spring quản lý Bean đó
 *      - Nếu không có componentModel = "spring", thì MapStruct vẫn sinh ra class OptionValueMapperImpl,
 *      nhưng Spring Boot không biết class đó tồn tại → không thể @Autowired => Khi DI vào Service sẽ bị null.
 *    
 * 🚀 3️⃣ Khi có componentModel = "spring"
 *      - MapStruct tự động thêm annotation @Component vào class triển khai (OptionValueMapperImpl).
 *      - Khi đó Spring Boot:
 *          + Phát hiện mapper này trong quá trình component scan.
 *          + Cho phép inject qua @Autowired hoặc constructor injection.
 * 
 * 📘 4️⃣ Các chế độ componentModel khác
 * 
 *  | componentModel | Mô tả                                                  |
    | -------------- | ------------------------------------------------------ |
    | `"default"`    | Không dùng DI, phải gọi `Mappers.getMapper()` thủ công |
    | `"spring"`     | Tự động thêm `@Component`, dùng được với `@Autowired`  |
    | `"jsr330"`     | Dùng `@Inject` thay vì `@Autowired`                    |
    | `"cdi"`        | Dùng `@Named` và `@Inject` (cho Jakarta EE)            |

 * 📘 5️⃣ Mục đích thật sự của uses = {...}
 *      - uses giúp MapStruct tái sử dụng mapper khác để map những thuộc tính phức tạp hoặc nested object.
 *      - @Mapper(componentModel = "spring", uses = { CategoryMapper.class })
 */
@Mapper(componentModel = "spring")
public interface OptionValueMapper extends EntityMapper<GetOptionValueDTO, OptionValue> {
    OptionValueMapper INSTANCE = Mappers.getMapper(OptionValueMapper.class);

    // Map từ Entity sang DTO
    GetOptionValueDTO toDto(OptionValue entity);
    List<GetOptionValueDTO> toDto(List<OptionValue> optionValues);

    // Map từ DTO sang Entity
    OptionValue toEntity(GetOptionValueDTO dto);
}
