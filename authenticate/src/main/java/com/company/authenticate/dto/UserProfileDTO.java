package com.company.authenticate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing User details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO {

    @NotBlank(message = "User Id cannot be empty")
    @Size(max = 10, message = "User Id must be at most 10 digits")
    @Schema(description = "User Id of the user", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;

    @NotBlank(message = "User Name cannot be empty")
    @Size(max = 50, message = "User Name must be at most 50 characters")
    @Schema(description = "User Name of the user", example = "admin01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @NotBlank(message = "Password cannot be empty")
    @Size(max = 8, message = "Password must be at most 8 characters")
    @Schema(description = "Password of the user", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Role cannot be empty")
    @Pattern(regexp = "^(admin|user)$", message = "Role must be admin or user")
    @Schema(description = "Role of the User", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"admin", "user"})
    private String role;

    @NotBlank(message = "Enabled option cannot be empty")
    @Pattern(regexp = "^(true|false)$", message = "Enabled option must be true or false")
    @Schema(description = "Option to show whether the userId is enabled or not", example = "true", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"true", "false"})
    private boolean enabled;
}
