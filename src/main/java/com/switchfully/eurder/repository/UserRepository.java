package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.UserType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private List<User> userList;

    public UserRepository() {
        this.userList = new ArrayList<>();
        userList.add(new User("admin", "adminLN", "admin@hotmail.com", "UNKNOWN", "UNKOWN").setUserType(UserType.ADMIN));
    }

    public List<User> getUserList() {
        return userList;
    }

    public User addUser(User user) {
        userList.add(user);
        return user;
    }
}
