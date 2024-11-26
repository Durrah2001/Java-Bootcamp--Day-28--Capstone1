package org.example.amazonwebsitecapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    @NotEmpty(message = "Product ID can not be empty!")
    private String id;

    @NotEmpty(message = "Product name can not be empty!")
    @Size(min = 4, message = "Product name must be more than \"3\" characters!")
    private String name;

    @Positive(message = "Price must be a positive number!")
    private double price;

    @NotEmpty(message = "Category ID can not be empty!")
    private String category_id;


}
