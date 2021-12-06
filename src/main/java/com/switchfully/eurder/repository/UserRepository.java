package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    public List<User> userList;

    public UserRepository() {
        this.userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public User addUser(User user) {
        userList.add(user);
        return user;
    }
}
