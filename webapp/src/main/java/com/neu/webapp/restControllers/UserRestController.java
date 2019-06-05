package com.neu.webapp.restControllers;

import com.neu.webapp.models.User;
import com.neu.webapp.services.UserService;
import com.neu.webapp.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String welcome(HttpServletRequest request){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return "Welcome "+request.getRemoteUser()+", current time: "+sdf.format(cal.getTime());
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public User register(@Valid @RequestBody User user, BindingResult errors, HttpServletResponse response) throws Exception{
        bookValidator.
        if(errors.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            userService.register(user);
            return user;
        }
    }
}
