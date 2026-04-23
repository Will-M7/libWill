package libreria.com.libwill.mapper;

import libreria.com.libwill.dto.response.BoletaResponseDTO;
import libreria.com.libwill.entity.BoletaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoletaMapper {

    BoletaResponseDTO toResponseDTO(BoletaEntity boleta);

    List<BoletaResponseDTO> toResponseDTOList(List<BoletaEntity> boletas);
}