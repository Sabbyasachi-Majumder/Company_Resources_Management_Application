package com.company.authenticate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for user profile creation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileRequestDTO {
    @Valid
    @NotEmpty(message = "User Profile details list cannot be empty")
    @JsonProperty("userProfileList")
    @Schema(description = "List of employee details", requiredMode = Schema.RequiredMode.REQUIRED)
    ArrayList<UserProfileDTO> userProfileList;
}