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

    @GetMapping("/")
    public ResponseEntity<String> welcome(HttpServletRequest request){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return ResponseEntity.status(HttpStatus.OK).body(
                "Welcome  current time: "+sdf.format(cal.getTime())
        );
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @PostMapping("/user/register")
    public ResponseEntity<RegistrationStatus> register(@Valid @RequestBody User user, BindingResult errors, HttpServletResponse response) throws Exception{
        RegistrationStatus registrationStatus;

        if(errors.hasErrors()) {
            registrationStatus = userService.getErrorStatus(errors);
            return new ResponseEntity(registrationStatus, HttpStatus.BAD_REQUEST);
        }else {
            registrationStatus = new RegistrationStatus();
            userService.register(user);
            return new ResponseEntity(registrationStatus, HttpStatus.CREATED);
        }
    }
}
