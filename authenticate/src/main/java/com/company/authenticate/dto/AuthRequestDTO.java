package com.company.authenticate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Request DTO for user authentication")
public class AuthRequestDTO {
    @Schema(description = "Username of the user", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @Schema(description = "Password of the user", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
