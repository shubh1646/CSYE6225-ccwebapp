package com.neu.webapp.services;

import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class BookServiceTest {
    @InjectMocks
    BookService bookServiceTest;

    @Mock
    BookRepository bookRepositoryTest;


    private static Book BOOK;

    @Before
    public void setUp() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("The Adventures of Sherlock Holmes");
        book.setIsbn("ISB123");
        book.setAuthor("Arthur Conan Doyle");
        book.setQuantity((short)12);
        this.BOOK = book;
    }

    @Test
    public  void getByIdTest(){
        //add the behavior of book service to get the id
        Mockito.when(bookRepositoryTest.findById(BOOK.getId())).thenReturn(Optional.of(BOOK));
        Book book = bookServiceTest.getBookById(BOOK.getId());
        Assertions.assertThat(book).isEqualTo(BOOK);
   }

   @Test
    public void deleteBookByIdTest(){
        Mockito.when(bookRepositoryTest.findById(BOOK.getId())).thenReturn(Optional.of(BOOK));
        bookServiceTest.deleteById(BOOK.getId());
        Mockito.verify(bookRepositoryTest).deleteById(BOOK.getId());
   }

}
