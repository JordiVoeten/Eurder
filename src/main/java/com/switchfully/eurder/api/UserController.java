package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.UserMapper;
import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.user.CreateUserDto;
import com.switchfully.eurder.domain.user.UserDto;
import com.switchfully.eurder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
        logger.info("Method createUser called");
        User newUser = userMapper.mapCreateUserDtoToUser(createUserDto);
        User createdUser = userService.createUser(newUser);
        UserDto userDto = userMapper.mapUserToDto(createdUser);
        logger.info("Method createUser Executed successfully");
        return userDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(){
        return userService.getUsers().stream()
                .map(userMapper::mapUserToDto)
                .toList();
    }
}
