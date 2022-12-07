package dev.decagon.fashionblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
