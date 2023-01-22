package com.assignment.readingisgood.services;

import com.assignment.readingisgood.dtos.AddBookRequest;
import com.assignment.readingisgood.exceptions.BookAlreadyExistException;
import com.assignment.readingisgood.exceptions.BookNotFoundException;
import com.assignment.readingisgood.exceptions.OutOfStockException;
import com.assignment.readingisgood.models.Book;
import com.assignment.readingisgood.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService{
    private final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Autowired
    private BookRepository bookRepository;
    @Override
    public Book addBook(AddBookRequest addBookRequest) throws BookAlreadyExistException {
        logger.info("Inside AddBook Service");
        if(bookRepository.findByNameAndAuthor(addBookRequest.getName(), addBookRequest.getAuthor()).isEmpty()) {
            logger.info("Book not found in database. Generating ID for new Book.");
            String uuid = UUID.randomUUID().toString();
            Book book = new Book(uuid,addBookRequest);
            logger.info("Saving book {} into database.",book);
            bookRepository.save(book);
            logger.info("Book {} saved into database.",book);
            return book;
        }else{
            String message = String.format("Book with name = %s and author = %s is already present.",addBookRequest.getName(),addBookRequest.getAuthor());
            logger.error(message);
            throw new BookAlreadyExistException(message);
        }
    }

    @Override
    public synchronized Book updateQuantity(String bookId, Integer quantity) throws BookNotFoundException, OutOfStockException {
        logger.info("Inside Update Book Quantity Service");
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            logger.info("Book with id = {} already exist.",book.getId());
            logger.info("Initial book quantity = {}.",book.getQuantity());
            int updatedQuantity = book.getQuantity() + quantity;
            if( updatedQuantity <= 0){
                String message = String.format("Book with id = %s is out of stock.",updatedQuantity);
                logger.error(message);
                throw new OutOfStockException(message);
            }else {
                book.setQuantity(updatedQuantity);
                bookRepository.save(book);
                logger.info("Final book quantity = {}.",book.getQuantity());
                return book;
            }
        }else {
            String message = String.format("Book with id = %s not found.",bookId);
            logger.error(message);
            throw new BookNotFoundException(message);
        }
    }
}
