package com.assignment.readingisgood.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NonNull
@AllArgsConstructor
@ToString
public class UpdateBookQuantityRequest {
    @NonNull
    @NotBlank(message = "Id is mandatory.")
    private String id;
    @NonNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 1, message = "Quantity should be greater than 0.")
    private int quantity;
}
