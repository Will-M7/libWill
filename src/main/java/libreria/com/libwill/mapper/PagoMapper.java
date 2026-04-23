package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.response.PagoResponseDTO;
import libreria.com.libwill.dto.response.UsuarioInfoDTO;
import libreria.com.libwill.entity.PagoEntity;
import libreria.com.libwill.entity.RolEntity;
import libreria.com.libwill.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(source = "validadoPor", target = "validadoPor")
    PagoResponseDTO toResponseDTO(PagoEntity pago);

    List<PagoResponseDTO> toResponseDTOList(List<PagoEntity> pagos);

    UsuarioInfoDTO toUsuarioInfoDTO(UsuarioEntity usuario);

    default List<String> rolesToStrings(Set<RolEntity> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(RolEntity::getNombre)
                .collect(Collectors.toList());
    }
}