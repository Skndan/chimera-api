package com.skndan.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import jakarta.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(title = "My API", version = "1.0"),
        security = @SecurityRequirement(name = "Keycloak") // Apply globally
)
@SecurityScheme(
        securitySchemeName = "Keycloak",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class OpenApiConfig extends Application {
}