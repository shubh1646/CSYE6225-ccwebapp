package com.neu.webapp.restControllers;

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
    public ResponseEntity<?> welcome(HttpServletRequest request){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return ResponseEntity.status(HttpStatus.OK).body(
                "Welcome "+request.getRemoteUser()+", current time: "+sdf.format(cal.getTime())
        );
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult errors, HttpServletResponse response) throws Exception{
        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
    }
}
