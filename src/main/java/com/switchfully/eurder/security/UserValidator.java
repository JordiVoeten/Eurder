package com.switchfully.eurder.security;

import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.user.UserType;
import com.switchfully.eurder.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserValidator {
    private final UserService userService;
    private final Map<Feature, UserType> featureMap = initializeFeatureMap();

    private Map<Feature, UserType> initializeFeatureMap() {
        Map<Feature, UserType> map = new HashMap<>();
        // features without validation (Story 1: create a customer account) any guest should be able

        // Features for the item controller
        map.put(Feature.ADD_ITEM, UserType.ADMIN);

        // Features for the user controller
        map.put(Feature.VIEW_ALL_CUSTOMERS, UserType.ADMIN);
        map.put(Feature.VIEW_CUSTOMER, UserType.ADMIN);

        // Features for the order controller
        map.put(Feature.ORDER_ITEMS, UserType.CUSTOMER);
        map.put(Feature.VIEW_ORDER_REPORT, UserType.CUSTOMER);

        return map;
    }

    public void assertUserTypeForFeature(Feature feature, String authorization) {
        UserType userTypeToCheck = parseAuthorizationString(authorization).getUserType();
        UserType minimumUserType = featureMap.get(feature);
        if (userTypeToCheck.getUserValue() < minimumUserType.getUserValue()) {
            throw new InvalidUserException("This user is not authorized to preform this action.");
        }
    }

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    private User parseAuthorizationString(String authorization) {
        if (authorization == null) {
            throw new InvalidUserException("The username or password is incorrect.");
        }
        String parsed = new String(Base64.getDecoder().decode(authorization.substring(authorization.indexOf(" ") + 1)));
        String email = parsed.substring(0, parsed.indexOf(":"));
        return userService.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new InvalidUserException("The username or password is incorrect."));
    }

}
