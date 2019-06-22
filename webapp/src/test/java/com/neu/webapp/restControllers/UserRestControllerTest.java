//package com.neu.webapp.restControllers;
//
//import com.neu.webapp.services.UserService;
//import com.neu.webapp.validators.UserValidator;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//@RunWith(SpringRunner.class)
//public class UserRestControllerTest {
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private UserRestController userRestControllerTest;
//
//    @Mock
//    private UserService userServiceTest;
//
//    @Mock
//    private UserValidator userValidator;
//
//    @Before
//    public void setUp(){
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestControllerTest).build();
//    }
//
//    @Test
//    public void welcomeTest() throws Exception{
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("{ \"message\": \"Welcome  current time: "+sdf.format(cal.getTime())+"\" }"));
//    }
//}
