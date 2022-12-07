package dev.decagon.fashionblog.controller;

import dev.decagon.fashionblog.dto.AddUserDto;
import dev.decagon.fashionblog.dto.LoginUserDto;
import dev.decagon.fashionblog.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody AddUserDto addUserDto){

        userService.registerUser(addUserDto);

        return new ResponseEntity<>("New User Added Successfully", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginUserDto loginUserDto){

        userService.loginUser(loginUserDto);

        return new ResponseEntity<>("User Logged In Successfully", HttpStatus.OK);
    }
}
