package dev.decagon.fashionblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserDto {

    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;

}
