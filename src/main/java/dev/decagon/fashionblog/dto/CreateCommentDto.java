package dev.decagon.fashionblog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentDto {

    @NotBlank
    private String body;

    @NotNull
    private Long userId;
}
