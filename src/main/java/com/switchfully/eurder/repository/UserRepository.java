package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.UserType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository {
    private final List<User> userList;

    public UserRepository() {
        this.userList = new ArrayList<>();

        User admin = User.builder()
                .id(UUID.randomUUID().toString())
                .firstName("admin")
                .lastName("adminLN")
                .email("admin@hotmail.com")
                .address("unknown")
                .phoneNumber("unknown")
                .userType(UserType.ADMIN)
                .build();
         userList.add(admin);
    }

    public List<User> getUserList() {
        return userList;
    }

    public User addUser(User user) {
        userList.add(user);
        return user;
    }
}
