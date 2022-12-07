package dev.decagon.fashionblog.service.impl;

import dev.decagon.fashionblog.dto.AddUserDto;
import dev.decagon.fashionblog.dto.LoginUserDto;
import dev.decagon.fashionblog.exception.AlreadyExistException;
import dev.decagon.fashionblog.exception.BadRequestException;
import dev.decagon.fashionblog.exception.NotFoundException;
import dev.decagon.fashionblog.model.User;
import dev.decagon.fashionblog.repository.UserRepository;
import dev.decagon.fashionblog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;

    @Mock
    private ModelMapper mapper;


    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, mapper);
    }

    @Test
    void testAddUser() {
        //given
        AddUserDto userDto = new AddUserDto("Tom", "tom@email.com", "1234");
        User user = new User("Tom", "tom@email.com", "1234");
        //when
        given(mapper.map(userDto, User.class)).willReturn(user);
        underTest.registerUser(userDto);

        //then
        ArgumentCaptor<User> studentArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(studentArgumentCaptor.capture());

        User capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(user);
    }

    @Test
    void testAddStudentWhenEmailTaken() {
        //given
        AddUserDto userDto = new AddUserDto("Tom", "tom@email.com", "1234");

        given(userRepository.existsByEmail(userDto.getEmail()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.registerUser(userDto))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage("Email Already Exist");

        verify(userRepository, never()).save(any());

    }

    @Test
    void testLoginUser() {
        //given
        LoginUserDto userDto = new LoginUserDto();
        userDto.setEmail("tom@email.com");
        userDto.setPassword("1234");

        User user = new User("Tom", "tom@email.com", "1234");
        given(userRepository.findUserByEmail(userDto.getEmail())).willReturn(Optional.of(user));

        //when
        underTest.loginUser(userDto);

        //then
        verify(userRepository).findUserByEmail(userDto.getEmail());
    }

    @Test
    void testLoginUserNotFound() {
        //given
        LoginUserDto userDto = new LoginUserDto();
        userDto.setEmail("tom@email.com");
        userDto.setPassword("1234");

        //when
        //then
        assertThatThrownBy(() -> underTest.loginUser(userDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with email: " + userDto.getEmail() + " Not Found");
    }

    @Test
    void testLoginUserPasswordIncorrect() {
        //given
        LoginUserDto userDto = new LoginUserDto();
        userDto.setEmail("tom@email.com");
        userDto.setPassword("12345");

        User user = new User("Tom", "tom@email.com", "1234");
        given(userRepository.findUserByEmail(userDto.getEmail())).willReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.loginUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Incorrect Password");
    }
}