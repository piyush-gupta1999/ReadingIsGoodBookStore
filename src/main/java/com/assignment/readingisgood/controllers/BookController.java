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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static com.assignment.readingisgood.utils.Constants.CONTROLLER_EXCEPTION_MESSAGE;


@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/books/add")
    public ResponseEntity<Object> addBook(@Valid @RequestBody AddBookRequest addBookRequest){
        try{
            logger.info("Inside AddBook Controller with request body: {}",addBookRequest);
            Book book =  bookService.addBook(addBookRequest);
            logger.info("Final Book object received in AddBookController: {}",book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookAlreadyExistException bookAlreadyExistException){
            logger.error("Exception occurred in AddBook Controller: {}",bookAlreadyExistException.getMessage());
            return new ResponseEntity<>(bookAlreadyExistException.getMessage(), HttpStatus.CONFLICT);
        } catch (RuntimeException runtimeException){
            logger.error("Exception occurred in AddBook Controller: {}",runtimeException.getMessage());
            return new ResponseEntity<>(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/books/update_quantity")
    public ResponseEntity<Object> updateBookQuantity(@Valid @RequestBody UpdateBookQuantityRequest updateBookQuantityRequest){
        String controllerName = "UpdateBookQuantity";
        try{
            logger.info("Inside {} Controller with request body: {}",controllerName,updateBookQuantityRequest);
            Book book =  bookService.updateQuantity(updateBookQuantityRequest.getId(), updateBookQuantityRequest.getQuantity());
            logger.info("Final Book object received in {} Controller: {}",controllerName,book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException exception) {
            logger.error(CONTROLLER_EXCEPTION_MESSAGE,controllerName,exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OutOfStockException exception) {
            logger.error(CONTROLLER_EXCEPTION_MESSAGE,controllerName,exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        } catch (RuntimeException exception) {
            logger.error(CONTROLLER_EXCEPTION_MESSAGE,controllerName,exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
