package com.switchfully.eurder.domain.user;

public enum UserType {
    CUSTOMER(1),
    ADMIN(2);

    private final int userValue;

    UserType(int userValue) {
        this.userValue = userValue;
    }

    public int getUserValue() {
        return userValue;
    }
}
