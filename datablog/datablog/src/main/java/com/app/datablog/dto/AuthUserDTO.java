package com.app.datablog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Authentication DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO {

    @Schema(description = "Authentication DTO email", example = "nahuelpierini@gmail.com")
    @NotBlank(message = "The email address is required")
    @Email(message = "The email address must be valid")
    private String email;

    @Schema(description = "Authentication DTO password", example = "123456")
    @NotBlank(message = "The password is required")
    @Size(min = 6, max = 20, message = "The password must be between 6 and 20 characters")
    private String password;
}

