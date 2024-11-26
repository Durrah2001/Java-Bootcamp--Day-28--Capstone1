package org.example.amazonwebsitecapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {

    @NotEmpty(message = "Merchant ID can not be empty!")
    private String id;

    @NotEmpty(message = "Merchant name can not be empty!")
    @Size(min= 4, message = "Merchant name must be more than \"3\" characters!")
    private String name;

    @PositiveOrZero(message = "Number of rating must be 0 or a positive number!")
    private int numOfRating;

    @PositiveOrZero(message = "Number of rating must be 0 or a positive number!")
    private double ratingAvg;





}
