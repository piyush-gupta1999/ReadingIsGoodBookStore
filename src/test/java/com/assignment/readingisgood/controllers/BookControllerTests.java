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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class BookControllerTests {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void testAddBookInvalidInputs() throws Exception {
        String json = "{\"name\":\"book1\",\"author\":\"author1\",\"quantity\":\"abc\",\"price\":\"def\"}";

        MockMvc mockMvc = standaloneSetup(bookController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addBook_ValidRequest_ShouldReturnOk() throws BookAlreadyExistException {
        AddBookRequest addBookRequest = new AddBookRequest("Mock Book", "Mock Author", 10, 100);

        Book mockBook = new Book();
        mockBook.setName("Mock Book");
        mockBook.setAuthor("Mock Author");
        mockBook.setQuantity(10);
        mockBook.setPrice(100);

        when(bookService.addBook(any(AddBookRequest.class))).thenReturn(mockBook);

        ResponseEntity<Object> response = bookController.addBook(addBookRequest);

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

        ResponseEntity<Object> response = bookController.addBook(addBookRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void addBook_RuntimeError_ShouldReturnInternalServerError() throws BookAlreadyExistException {
        AddBookRequest addBookRequest = new AddBookRequest("Mock Book", "Mock Author", 10, 100);

        String expectedMessage = String.format("Exception occurred in adding new Book with name = %s and author = %s.",addBookRequest.getName(),addBookRequest.getAuthor());

        when(bookService.addBook(any(AddBookRequest.class))).thenAnswer( invocationOnMock ->
        {
            String message = String.format("Exception occurred in adding new Book with name = %s and author = %s.",addBookRequest.getName(),addBookRequest.getAuthor());
            throw new RuntimeException(message);
        });

        ResponseEntity<Object> response = bookController.addBook(addBookRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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

        ResponseEntity<Object> response = bookController.updateBookQuantity(updateBookQuantityRequest);

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

        ResponseEntity<Object> response = bookController.updateBookQuantity(updateBookQuantityRequest);

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

        ResponseEntity<Object> response = bookController.updateBookQuantity(updateBookQuantityRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void updateBookQuantity_RuntimeError_ShouldReturnInternalServerError() throws OutOfStockException, BookNotFoundException {
        UpdateBookQuantityRequest updateBookQuantityRequest = new UpdateBookQuantityRequest("id1", 10);
        String expectedMessage = String.format("Exception occurred in updating quantity for book id = %s",updateBookQuantityRequest.getId());
        when(bookService.updateQuantity(anyString(),anyInt())).thenAnswer(invocationOnMock -> {
            String message = String.format("Exception occurred in updating quantity for book id = %s",updateBookQuantityRequest.getId());
            throw new RuntimeException(message);
        });

        ResponseEntity<Object> response = bookController.updateBookQuantity(updateBookQuantityRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

}
