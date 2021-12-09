package com.switchfully.eurder.domain.user.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;

    public CreateUserDto() {
    }

    public CreateUserDto(String firstName, String lastName, String email, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
