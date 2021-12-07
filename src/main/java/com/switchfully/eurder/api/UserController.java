package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.UserMapper;
import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import com.switchfully.eurder.security.Feature;
import com.switchfully.eurder.security.UserValidator;
import com.switchfully.eurder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserMapper userMapper;
    private final UserService userService;
    private final UserValidator userValidator;

    public UserController(UserMapper userMapper, UserService userService, UserValidator userValidator) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
        logger.info("Method createUser called");
        User newUser = userMapper.mapCreateUserDtoToUser(createUserDto);
        User createdUser = userService.createUser(newUser);
        UserDto userDto = userMapper.mapUserToDto(createdUser);
        logger.info("Method createUser executed successfully");
        return userDto;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestHeader(required = false) String authorization) {
        logger.info("Method getUsers called");
        userValidator.assertUserTypeForFeature(Feature.VIEW_ALL_CUSTOMERS, authorization);
        List<UserDto> userDtoList = userService.getUsers().stream()
                .map(userMapper::mapUserToDto)
                .toList();
        logger.info("Method getUsers executed successfully");
        return userDtoList;
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("id") String id, @RequestHeader(required = false) String authorization) {
        logger.info("Method getUser called");
        userValidator.assertUserTypeForFeature(Feature.VIEW_CUSTOMER, authorization);
        User user = userService.getUserBy(id);
        UserDto userDto = userMapper.mapUserToDto(user);
        logger.info("Method getUser executed successfully");
        return userDto;
    }
}
