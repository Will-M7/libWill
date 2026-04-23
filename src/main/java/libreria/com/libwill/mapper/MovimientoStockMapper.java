package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.response.MovimientoStockResponseDTO;
import libreria.com.libwill.dto.response.ProductoInfoDTO;
import libreria.com.libwill.dto.response.UsuarioInfoDTO;
import libreria.com.libwill.entity.MovimientoStockEntity;
import libreria.com.libwill.entity.ProductEntity;
import libreria.com.libwill.entity.RolEntity;
import libreria.com.libwill.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovimientoStockMapper {

    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "usuario", target = "usuario")
    MovimientoStockResponseDTO toResponseDTO(MovimientoStockEntity movimiento);

    List<MovimientoStockResponseDTO> toResponseDTOList(List<MovimientoStockEntity> movimientos);

    ProductoInfoDTO toProductoInfoDTO(ProductEntity producto);

    UsuarioInfoDTO toUsuarioInfoDTO(UsuarioEntity usuario);

    default List<String> rolesToStrings(Set<RolEntity> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(RolEntity::getNombre)
                .collect(Collectors.toList());
    }
}