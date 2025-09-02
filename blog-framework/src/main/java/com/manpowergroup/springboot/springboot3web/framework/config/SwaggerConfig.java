package com.manpowergroup.springboot.springboot3web.framework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Manpower Blog API")
                .description("Spring Boot 3 + MyBatis-Plus + Vue3")
                .version("1.0.0"));
    }
}
