package com.neu.webapp.repositories;

import com.neu.webapp.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {
    Book findBookById(UUID id);
}
