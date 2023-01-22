package com.assignment.readingisgood.repositories;

import com.assignment.readingisgood.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByNameAndAuthor(String name, String author);

}

