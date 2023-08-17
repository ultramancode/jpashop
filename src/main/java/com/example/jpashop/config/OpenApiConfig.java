package com.example.jpashop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI(){
    Info info = new Info()
        .title("스웨거 테스트용")
        .version("v0.0.1")
        .description("테스트중임");

    return new OpenAPI()
        .components(new Components())
        .info(info);
  }
}
