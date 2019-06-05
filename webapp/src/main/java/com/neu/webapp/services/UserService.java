package com.neu.webapp.services;

import com.neu.webapp.models.CustomUserDetails;
import com.neu.webapp.models.User;
import com.neu.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    public void register(User user) {
//        user.setHashedPassword(pwdEncoder.encode(user.getPassword()));
        user.setPassword(pwdEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = userRepository.findByEmailId(emailId);
        if(user==null) throw new UsernameNotFoundException("User with given emailId does not exist");
        else return new CustomUserDetails(user);
    }
}
