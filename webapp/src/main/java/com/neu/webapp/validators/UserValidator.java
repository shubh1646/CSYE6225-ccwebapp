package com.neu.webapp.validators;

import com.neu.webapp.models.User;
import com.neu.webapp.repositories.UserRepository;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

public class UserValidator implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "Email cant be null");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password cant be null");

        if(errors.hasErrors()) return;

        User user = (User) o;

        if(!user.getEmailId().matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) errors.rejectValue("emailId", "Email format is not proper");
        if(userRepository.isEmailPresent(user.getEmailId())!=0) errors.rejectValue("emailId", "Can not register with this emailId: already exists");

        if(errors.hasErrors()) return;

        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(user.getPassword()));
        if(!result.isValid()) errors.rejectValue("password", "Password must contain 1 Uppercase and 1 Lowercase and 1 Special and 1 Digit Character and WhiteSpaces");
    }
}