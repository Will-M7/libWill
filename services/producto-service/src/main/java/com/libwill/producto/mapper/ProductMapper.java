package com.libwill.producto.mapper;

import com.libwill.producto.dto.ProductDTO;
import com.libwill.producto.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductDTO dto);
}

/*@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDto(ProductEntity entity);
@Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductDTO dto);
}*/
