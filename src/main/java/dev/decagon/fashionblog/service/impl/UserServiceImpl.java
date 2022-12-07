package dev.decagon.fashionblog.service.impl;

import dev.decagon.fashionblog.dto.AddUserDto;
import dev.decagon.fashionblog.dto.LoginUserDto;
import dev.decagon.fashionblog.exception.AlreadyExistException;
import dev.decagon.fashionblog.exception.BadRequestException;
import dev.decagon.fashionblog.exception.NotFoundException;
import dev.decagon.fashionblog.model.User;
import dev.decagon.fashionblog.repository.UserRepository;
import dev.decagon.fashionblog.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public void registerUser(AddUserDto addUserDto) {

        boolean emailExists = userRepository.existsByEmail(addUserDto.getEmail());

        if (emailExists){
            throw new AlreadyExistException("Email Already Exist");
        }

        User user = mapper.map(addUserDto, User.class);

        userRepository.save(user);
    }

    @Override
    public void loginUser(LoginUserDto loginUserDto) {
        String email = loginUserDto.getEmail();
        Optional<User> foundUser = userRepository.findUserByEmail(email);

        if(foundUser.isEmpty()){
            throw new NotFoundException("User with email: " + email + " Not Found");
        }

        User user = foundUser.get();

        if(!user.getPassword().equals(loginUserDto.getPassword())){
            throw new BadRequestException("Incorrect Password");
        }
    }
}
