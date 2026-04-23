package libreria.com.libwill.service;

import libreria.com.libwill.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAll();

    ProductDTO getById(Long id);

    ProductDTO create(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    void delete(Long id);

    ProductDTO uploadImage(Long id, MultipartFile file);
}
