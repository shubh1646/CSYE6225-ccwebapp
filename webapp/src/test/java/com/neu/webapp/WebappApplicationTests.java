package com.neu.webapp;

import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.restControllers.BookRestController;
import com.neu.webapp.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Collection;

@RunWith(SpringRunner.class)

@WebMvcTest(value = BookRestController.class )

@SpringBootTest
public class WebappApplicationTests {
    @Autowired
    MockMvc mockmvc;


    @MockBean
    private BookService bookService;

    Book mockBook = new Book("python made easy", "978-0132126953", "andrew", 3);


    @Test
    public void contextLoads() throws Exception {

        Mockito.when(
                bookService.getAllBooks()).thenReturn(mockBook);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON);


        MvcResult result = mockmvc.perform(requestBuilder).andReturn();

        String expected = "{id : , titile , isbn , author , quantity : }";

        JSONAssert.assertEquals(expected,result.getResponse());

    }


}