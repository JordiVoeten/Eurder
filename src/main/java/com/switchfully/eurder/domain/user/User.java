package com.switchfully.eurder.domain.user;

import java.util.UUID;

public class User {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String address;
    private final String phoneNumber;

    public User(String firstName, String lastName, String email, String address, String phoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
