package dev.decagon.fashionblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private String role;
}
