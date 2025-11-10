package com.company.authenticate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Request DTO for user authentication")
public class AuthRequestDTO {
    @NotBlank(message = "must not be blank")
    @Schema(description = "Username of the user", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @NotBlank(message = "must not be blank")
    @Schema(description = "Password of the user", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
