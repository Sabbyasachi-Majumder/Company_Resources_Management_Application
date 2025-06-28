package com.company.authenticate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Response DTO containing JWT tokens after successful authentication")
public class AuthResponseDTO {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refreshToken;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public AuthResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }


}
