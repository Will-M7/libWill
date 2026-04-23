package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.ProductDTO;
import libreria.com.libwill.entity.ProductEntity;
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
