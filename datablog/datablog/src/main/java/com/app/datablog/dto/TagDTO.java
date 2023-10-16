package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Tag DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Tag DTO Id", example = "1")
    private Long id;

    @Schema(description = "Tag DTO title", example = "PYTHON")
    @NotBlank(message = "The title is required")
    private String title;

    @Schema(description = "Tag DTO metaTitle", example = "python")
    @NotBlank(message = "The metaTitle is required")
    private String metaTitle;

    @Schema(description = "Tag DTO slug", example = "python the new era")
    @NotBlank(message = "The slug is required")
    private String slug;
}