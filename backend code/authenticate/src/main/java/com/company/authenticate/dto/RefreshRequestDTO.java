package com.company.authenticate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Request DTO for refreshing JWT tokens")
public class RefreshRequestDTO {

    @Schema(description = "Refresh token to generate a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    public String refreshToken;

}
