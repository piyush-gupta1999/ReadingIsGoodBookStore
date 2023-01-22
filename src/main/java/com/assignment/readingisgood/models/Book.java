package com.assignment.readingisgood.models;

import com.assignment.readingisgood.dtos.AddBookRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOK")
public class Book {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String author;
    @NonNull
    @Min(0)
    private int quantity;
    @NonNull
    @DecimalMin("1.00")
    private double price;
    public Book(String id, AddBookRequest addBookRequest){
        this.id = id;
        this.name = addBookRequest.getName();
        this.author = addBookRequest.getAuthor();
        this.quantity = addBookRequest.getQuantity();
        this.price = addBookRequest.getPrice();
    }
}
