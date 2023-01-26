package com.assignment.readingisgood.controllers;

import com.assignment.readingisgood.dtos.AddBookRequest;
import com.assignment.readingisgood.dtos.UpdateBookQuantityRequest;
import com.assignment.readingisgood.exceptions.BookAlreadyExistException;
import com.assignment.readingisgood.exceptions.BookNotFoundException;
import com.assignment.readingisgood.exceptions.OutOfStockException;
import com.assignment.readingisgood.models.Book;
import com.assignment.readingisgood.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void addBook_ValidRequest_ShouldReturnOk() throws BookAlreadyExistException {
        AddBookRequest addBookRequest = new AddBookRequest("Mock Book", "Mock Author", 10, 100);

        Book mockBook = new Book();
        mockBook.setName("Mock Book");
        mockBook.setAuthor("Mock Author");
        mockBook.setQuantity(10);
        mockBook.setPrice(100);

        when(bookService.addBook(any(AddBookRequest.class))).thenReturn(mockBook);

        ResponseEntity response = bookController.addBook(addBookRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBook, response.getBody());
    }

    @Test
    void addBook_BookAlreadyExist_ShouldReturnConflictRequest() throws BookAlreadyExistException {
        AddBookRequest addBookRequest = new AddBookRequest("Mock Book", "Mock Author", 10, 100);

        String expectedMessage = String.format("Book with name = %s and author = %s is already present.",addBookRequest.getName(),addBookRequest.getAuthor());

        when(bookService.addBook(any(AddBookRequest.class))).thenAnswer( invocationOnMock ->
        {
            String message = String.format("Book with name = %s and author = %s is already present.",addBookRequest.getName(),addBookRequest.getAuthor());
            throw new BookAlreadyExistException(message);
        });

        ResponseEntity response = bookController.addBook(addBookRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void updateBookQuantity_ValidRequest_ShouldReturnOk() throws OutOfStockException, BookNotFoundException {
        UpdateBookQuantityRequest updateBookQuantityRequest = new UpdateBookQuantityRequest("id1", 10);

        Book mockBook = new Book();
        mockBook.setId("id1");
        mockBook.setName("Mock Book");
        mockBook.setAuthor("Mock Author");
        mockBook.setQuantity(15);

        when(bookService.updateQuantity(anyString(),anyInt())).thenReturn(mockBook);

        ResponseEntity response = bookController.updateBookQuantity(updateBookQuantityRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBook, response.getBody());
    }

    @Test
    void updateBookQuantity_BookNotFound_ShouldReturnNotFoundError() throws OutOfStockException, BookNotFoundException {
        UpdateBookQuantityRequest updateBookQuantityRequest = new UpdateBookQuantityRequest("id1", 10);
        String expectedMessage = String.format("Book with id = %s is not found.",updateBookQuantityRequest.getId());
        when(bookService.updateQuantity(anyString(),anyInt())).thenAnswer(invocationOnMock -> {
            String message = String.format("Book with id = %s is not found.",updateBookQuantityRequest.getId());
            throw new BookNotFoundException(message);
        });

        ResponseEntity response = bookController.updateBookQuantity(updateBookQuantityRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println(response.getBody());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void updateBookQuantity_OutOfStock_ShouldReturnOkError() throws OutOfStockException, BookNotFoundException {
        UpdateBookQuantityRequest updateBookQuantityRequest = new UpdateBookQuantityRequest("id1", 10);
        String expectedMessage = String.format("Book with id = %s is out of stock.",updateBookQuantityRequest.getId());
        when(bookService.updateQuantity(anyString(),anyInt())).thenAnswer(invocationOnMock -> {
            String message = String.format("Book with id = %s is out of stock.",updateBookQuantityRequest.getId());
            throw new OutOfStockException(message);
        });

        ResponseEntity response = bookController.updateBookQuantity(updateBookQuantityRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

}
