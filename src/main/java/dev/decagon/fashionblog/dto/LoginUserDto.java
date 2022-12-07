package dev.decagon.fashionblog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {

    @Email
    private String email;
    @NotBlank
    private String password;
}
