package com.company.authenticate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponseDTO {

    @JsonProperty("userProfileList")
    @Schema(description = "List of user details returned in the response", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<UserProfileDTO> userProfileList;

    @JsonProperty("apiResponse")
    @Schema(description = "List of API responses for individual operations (e.g., add, update, delete)", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<ApiResponseDTO<UserProfileResponseDTO>> apiResponse;
}
