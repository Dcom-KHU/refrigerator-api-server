package dcom.refrigerator.api.global.security.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://20.38.46.151:8080")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(86400)
                .allowCredentials(true);
    }
}