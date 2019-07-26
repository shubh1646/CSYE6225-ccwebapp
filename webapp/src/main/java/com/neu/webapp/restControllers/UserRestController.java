package com.neu.webapp.restControllers;

import com.neu.webapp.errors.RegistrationStatus;
import com.neu.webapp.models.User;
import com.neu.webapp.services.UserService;
import com.neu.webapp.validators.UserValidator;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
public class UserRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private StatsDClient metricsClient;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }


    @GetMapping("/")
    public ResponseEntity<String> welcome(HttpServletRequest request, Principal principal) throws Exception{
        metricsClient.incrementCounter("endpoint./.http.get");
        LOGGER.info(principal.getName()+" User Authenticated");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String message = "Welcome  current time: "+sdf.format(cal.getTime());
        return ResponseEntity.status(HttpStatus.OK).body("{ \"message\": \""+message+"\" }");
    }

    @PostMapping("/user/register")
public ResponseEntity<RegistrationStatus> register(@Valid @RequestBody User user, BindingResult errors, HttpServletResponse response) {
        metricsClient.incrementCounter("endpoint./user/register.http.post");
        RegistrationStatus registrationStatus;
        if(errors.hasErrors()) {
            LOGGER.warn("User Registration Failed");
            registrationStatus = userService.getRegistrationStatus(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registrationStatus);
        }else {
            LOGGER.info("User Registration Successful");
            registrationStatus = new RegistrationStatus();
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationStatus);
        }
    }
}
