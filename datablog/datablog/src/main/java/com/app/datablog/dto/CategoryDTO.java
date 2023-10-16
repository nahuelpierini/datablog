package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Category DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Category DTO Id", example = "1")
    private Long id;

    @Schema(description = "Category DTO title", example = "JAVA")
    @NotBlank(message = "The title is required")
    private String title;

    @Schema(description = "Category DTO metaTitle", example = "java the best programming language")
    @NotBlank(message = "The metaTitle is required")
    private String metaTitle;

    @Schema(description = "Category DTO slug", example = "java")
    @NotBlank(message = "The slug is required")
    private String slug;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Category DTO childCategories", example = "arrays, polymorphism, abstract class, object")
    private List<CategoryDTO> childCategories;
}

