package com.assignment.readingisgood.controllers;

import com.assignment.readingisgood.dtos.AddBookRequest;
import com.assignment.readingisgood.dtos.UpdateBookQuantityRequest;
import com.assignment.readingisgood.exceptions.BookAlreadyExistException;
import com.assignment.readingisgood.exceptions.BookNotFoundException;
import com.assignment.readingisgood.exceptions.OutOfStockException;
import com.assignment.readingisgood.models.Book;
import com.assignment.readingisgood.services.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/books/add")
    public ResponseEntity addBook(@Valid @RequestBody AddBookRequest addBookRequest){
        try{
            logger.info("Inside AddBook Controller with request body: {}",addBookRequest);
            Book book =  bookService.addBook(addBookRequest);
            logger.info("Final Book object received in AddBookController: {}",book);
            return ResponseEntity.ok().body(book);
        } catch (BookAlreadyExistException | RuntimeException bookAlreadyExistException){
            logger.error("Exception occurred in AddBook Controller: {}",bookAlreadyExistException.getMessage());
            return ResponseEntity.badRequest().body(bookAlreadyExistException.getMessage());
        }
    }
    @PostMapping("/books/update_quantity")
    public ResponseEntity updateBookQuantity(@Valid @RequestBody UpdateBookQuantityRequest updateBookQuantityRequest){
        try{
            logger.info("Inside UpdateBookQuantity Controller with request body: {}",updateBookQuantityRequest);
            Book book =  bookService.updateQuantity(updateBookQuantityRequest.getId(), updateBookQuantityRequest.getQuantity());
            logger.info("Final Book object received in UpdateBookQuantity Controller: {}",book);
            return ResponseEntity.ok().body(book);
        } catch (BookNotFoundException | OutOfStockException | RuntimeException exception) {
            logger.error("Exception occurred in UpdateBookQuantity Controller: {}",exception.getMessage());
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}