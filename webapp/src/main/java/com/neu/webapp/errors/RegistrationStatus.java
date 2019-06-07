package com.neu.webapp.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationStatus {
    private String emailIdError;
    private String passwordError;

    public RegistrationStatus() {
        emailIdError = "-";
        passwordError = "-";
    }

    public RegistrationStatus(String emailIdError, String passwordError) {
        this.emailIdError = emailIdError;
        this.passwordError = passwordError;
    }


}
