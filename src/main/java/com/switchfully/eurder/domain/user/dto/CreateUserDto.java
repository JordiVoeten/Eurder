package com.switchfully.eurder.domain.user.dto;

public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public CreateUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public CreateUserDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CreateUserDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
