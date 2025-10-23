package vn.clothing.fashion_shop.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface EntityMapper<D,E> {
    D toDto (E e);
    E toEntity (D d);

    List<E> toEntity (List<D> d);
    List<D> toDto (List<E> e);

    /*
     * Đây là method update một phần (partial update).
     * MapStruct sẽ chỉ map những field không null từ dto vào entity.
     * Field nào null → bỏ qua (không ghi đè).
     */
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);

}
