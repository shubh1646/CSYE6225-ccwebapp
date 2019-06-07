package com.neu.webapp.validators;

import com.neu.webapp.models.User;
import com.neu.webapp.services.UserService;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "emailId required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password required");

        if(errors.hasErrors()) return;

        User user = (User) o;

        if(!user.getEmailId().matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) errors.rejectValue("emailId", "emailId format is wrong");

        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(9, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(user.getPassword()));
        if(!result.isValid()) errors.rejectValue("password", "Password must 9-30 characters long and must have Uppercase, Lowercase, Special characters and Digits");

        if(errors.hasErrors()) return;

        if(userService.isEmailPresent(user.getEmailId())) errors.rejectValue("emailId", "account already exists");

    }
}