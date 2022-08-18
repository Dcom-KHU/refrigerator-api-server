package dcom.refrigerator.api.global.security.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://3.138.230.191:8080/")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(86400)
                .allowCredentials(true);
    }
}