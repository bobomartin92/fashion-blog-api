package dev.decagon.fashionblog.service;

import dev.decagon.fashionblog.dto.AddUserDto;
import dev.decagon.fashionblog.dto.LoginUserDto;

public interface UserService {
    void registerUser(AddUserDto addUserDto);

    void loginUser(LoginUserDto loginUserDto);
}
