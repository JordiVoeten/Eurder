package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.CreateUserDto;
import com.switchfully.eurder.domain.user.UserDto;
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
        return new UserDto()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setAddress(user.getAddress())
                .setPhoneNumber(user.getPhoneNumber());
    }
}
