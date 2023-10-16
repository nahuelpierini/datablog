package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Post DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post DTO Id", example = "1")
    private Long id;

    @Schema(description = "Post DTO title", example = "SQL")
    @NotBlank(message = "The title is required")
    private String title;

    @Schema(description = "Post DTO metaTitle", example = "sql")
    @NotBlank(message = "The metaTitle is required")
    private String metaTitle;

    @Schema(description = "Post DTO slug", example = "sql")
    @NotBlank(message = "The slug is required")
    private String slug;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post DTO createdAt", example = "2023-09-21T15:30:00Z", format = "date-time")
    private LocalDateTime createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post DTO createdAt", example = "2023-09-21T15:30:00Z", format = "date-time")
    private LocalDateTime updatedAt;

    @Schema(description = "Post DTO content", example = "This is the content of the post")
    private String content;

    @Schema(description = "Post DTO summary", example = "This is the summary of the post")
    private String summary;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post DTO userName", example = "Nahuel Pierini")
    private String userName;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post DTO userId", example = "1")
    private Long userId;

    private Set<TagDTO> tags = new HashSet<>();

    private CategoryDTO category;
}
