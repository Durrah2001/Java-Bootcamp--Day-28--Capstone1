package org.example.amazonwebsitecapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotEmpty(message = "Category ID can not be empty!")
    private String id;

    @NotEmpty(message = "Category name can not be empty!")
    @Size(min= 4, message = "Category name must be more than \"3\" characters!")
    private String name;








}
