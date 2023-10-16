package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "User DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User DTO Id", example = "1")
    private Long id;

    @Schema(description = "User DTO firstName", example = "Nahuel")
    @NotBlank(message = "The first name is required")
    private String firstName;

    @Schema(description = "User DTO lastName", example = "Pierini")
    @NotBlank(message = "The last name is required")
    private String lastName;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User DTO fullName", example = "Nahuel Pierini")
    private String fullName;

    @Schema(description = "User DTO email", example = "nahuelpierini@gmail.com")
    @NotBlank(message = "The email address is required")
    @Email(message = "The email address must be valid")
    private String email;

    @Schema(description = "User DTO password", example = "123456")
    @NotBlank(message = "The password is required")
    @Size(min = 6, max = 20, message = "The password must be between 6 and 20 characters")
    private String password;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User DTO registeredAt", example = "2023-09-21 10:54:14", format = "date-time")
    private LocalDateTime registeredAt;

    private boolean isActive;

    private RoleDTO role;
}