package libreria.com.libwill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/imagenes/pagos/**")
                .addResourceLocations("file:./static.imagenes/pagos/")
                .setCachePeriod(0);

        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:./static.imagenes/")
                .setCachePeriod(0);
    }
}