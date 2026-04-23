package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.response.*;
import libreria.com.libwill.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VentaFisicaMapper {

    @Mapping(source = "vendedor", target = "vendedor")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "pago", target = "pago")
    @Mapping(source = "boleta", target = "boleta")
    VentaFisicaResponseDTO toResponseDTO(VentaFisicaEntity venta);

    List<VentaFisicaResponseDTO> toResponseDTOList(List<VentaFisicaEntity> ventas);

    @Mapping(source = "producto", target = "producto")
    VentaItemResponseDTO toItemResponseDTO(VentaItemEntity item);

    UsuarioInfoDTO toUsuarioInfoDTO(UsuarioEntity usuario);

    ProductoInfoDTO toProductoInfoDTO(ProductEntity producto);

    PagoResponseDTO toPagoResponseDTO(PagoEntity pago);

    BoletaResponseDTO toBoletaResponseDTO(BoletaEntity boleta);

    default List<String> rolesToStrings(Set<RolEntity> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(RolEntity::getNombre)
                .collect(Collectors.toList());
    }
}