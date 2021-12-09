package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapCreateUserDtoToUser(CreateUserDto createUserDto) {
        return new User(createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getAddress(),
                createUserDto.getPhoneNumber());
    }

    public UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
