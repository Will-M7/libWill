package libreria.com.libwill.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoInfoDTO {
    private Long id;
    private String name;
    private String presentation;
    private String imageUrl;
    private BigDecimal priceMinor;
    private BigDecimal priceMajor;
    private Integer stock;
    private Boolean active;
}