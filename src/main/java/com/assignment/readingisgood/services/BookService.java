package com.assignment.readingisgood.services;

import com.assignment.readingisgood.dtos.AddBookRequest;
import com.assignment.readingisgood.exceptions.BookAlreadyExistException;
import com.assignment.readingisgood.exceptions.BookNotFoundException;
import com.assignment.readingisgood.exceptions.OutOfStockException;
import com.assignment.readingisgood.models.Book;

public interface BookService {
    Book addBook(AddBookRequest addBookRequest) throws BookAlreadyExistException;

    Book updateQuantity(String bookId, Integer quantity) throws BookNotFoundException, OutOfStockException;
}
