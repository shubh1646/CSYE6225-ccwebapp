package com.neu.webapp.services;

import com.neu.webapp.models.User;
import com.neu.webapp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserService userServiceTest;

    @Mock
    UserRepository userRepositoryTest;

    @Mock
    BCryptPasswordEncoder pwdEncoder;

    private static User USER;

    @Before
    public void setUp() {
        this.USER = new User(UUID.randomUUID(), "cyril1811sebastian@gmail.com", "P@$$W0rd");
    }

    @Test
    public void registerTest() {
        userServiceTest.register(USER);
        Mockito.verify(userRepositoryTest).save(USER);
    }
}
