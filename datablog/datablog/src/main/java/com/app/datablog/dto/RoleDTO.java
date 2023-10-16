package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Role DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Role DTO Id", example = "1")
    private Long id;

    @Schema(description = "Role DTO name", example = "ADMIN")
    @NotBlank(message = "The name is required")
    private String name;

    @Schema(description = "Role DTO description", example = "Manage everything")
    private String description;
}

