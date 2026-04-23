package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.response.*;
import libreria.com.libwill.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoOnlineMapper {

    @Mapping(source = "usuario", target = "usuario")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "pago", target = "pago")
    @Mapping(source = "boleta", target = "boleta")
    PedidoOnlineResponseDTO toResponseDTO(PedidoOnlineEntity pedido);

    List<PedidoOnlineResponseDTO> toResponseDTOList(List<PedidoOnlineEntity> pedidos);

    @Mapping(source = "producto", target = "producto")
    PedidoItemResponseDTO toItemResponseDTO(PedidoItemEntity item);

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