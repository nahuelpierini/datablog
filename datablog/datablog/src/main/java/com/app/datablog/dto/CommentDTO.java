package com.app.datablog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Comment DTO Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment DTO Id", example = "1")
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Category DTO publishedAt", example = "2023-09-21T15:30:00Z", format = "date-time")
    private LocalDateTime publishedAt;

    @Schema(description = "Comment DTO content", example = "It is a great post")
    private String content;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment DTO childComments", example = "yes, you are right!")
    private List<CommentDTO> childComments;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment DTO userName", example = "Nahuel Pierini")
    private String userName;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment DTO idUser", example = "5")
    private Long idUser;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment DTO idPost", example = "10")
    private Long idPost;

}