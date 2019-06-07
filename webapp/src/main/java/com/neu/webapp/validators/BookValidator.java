package com.neu.webapp.validators;

import com.neu.webapp.models.Book;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "author required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "quantity required");
        if(errors.hasErrors()) return;

        Book book = (Book) o;

        if(!book.getIsbn().matches("\\d{3}+-\\d{10}+")) errors.rejectValue("isbn", "isbn format wrong");
        if(book.getQuantity()<1) errors.rejectValue("quantity", "quantity cant be less than 1");
    }
}
