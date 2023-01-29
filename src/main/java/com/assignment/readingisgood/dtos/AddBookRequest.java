package com.assignment.readingisgood.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NonNull
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddBookRequest {
    @NonNull
    @NotBlank(message = "Book name is mandatory.")
    private String name;
    @NonNull
    @NotBlank(message = "Author name is mandatory.")
    private String author;
    @NonNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 1, message = "Quantity should be greater than 0.")
    private int quantity;
    @NonNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @DecimalMin(value = "1.00", message = "Price should be grater than 1.00.")
    private double price;
}
