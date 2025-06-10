package leoric.pizzacipollastorage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "MenuItem ERP API",
                version = "1.0",
                description = "Swagger dokumentace pro MenuItem ERP backend"
        )
)
@Configuration
public class OpenApiConfig {
}