package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.ProductDTO;
import libreria.com.libwill.entity.ProductEntity;
import libreria.com.libwill.mapper.ProductMapper;
import libreria.com.libwill.repository.ProductRepository;
import libreria.com.libwill.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    private final String UPLOAD_DIR = "uploads/products";

    @Override
    public List<ProductDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProductDTO getById(Long id) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapper.toDto(entity);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {

        if (repository.existsBySku(dto.getSku())) {
            throw new RuntimeException("Ya existe un producto con el SKU ingresado");
        }

        ProductEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPresentation(dto.getPresentation());
        entity.setPriceMinor(dto.getPriceMinor());
        entity.setPriceMajor(dto.getPriceMajor());
        entity.setStock(dto.getStock());
        entity.setActive(dto.getActive());
        entity.setSku(dto.getSku());

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        repository.deleteById(id);
    }

    @Override
    public ProductDTO uploadImage(Long id, MultipartFile file) {

        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String original = file.getOriginalFilename();
            String extension = original.substring(original.lastIndexOf("."));
            String filename = "product_" + id + extension;

            Path destination = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            product.setImageUrl("/" + UPLOAD_DIR + "/" + filename);

            repository.save(product);

            return mapper.toDto(product);

        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
        }
    }
}
