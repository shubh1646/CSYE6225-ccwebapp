package com.neu.webapp.restControllers;

import com.neu.webapp.errors.RegistrationStatus;
import com.neu.webapp.models.User;
import com.neu.webapp.services.UserService;
import com.neu.webapp.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @GetMapping("/")
    public ResponseEntity<String> welcome(HttpServletRequest request) throws Exception{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String message = "Welcome  current time: "+sdf.format(cal.getTime());
        return ResponseEntity.status(HttpStatus.OK).body(
                "{ \"message\": \""+message+"\" }"
        );
    }

    @PostMapping("/user/register")
    public ResponseEntity<RegistrationStatus> register(@Valid @RequestBody User user, BindingResult errors, HttpServletResponse response) throws Exception{
        RegistrationStatus registrationStatus;

        if(errors.hasErrors()) {
            registrationStatus = userService.getRegistrationStatus(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registrationStatus);
        }else {
            registrationStatus = new RegistrationStatus();
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationStatus);
        }
    }
}
