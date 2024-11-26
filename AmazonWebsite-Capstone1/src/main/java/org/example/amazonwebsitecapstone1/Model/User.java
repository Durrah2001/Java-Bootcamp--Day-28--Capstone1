package org.example.amazonwebsitecapstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    @NotEmpty(message = "User ID can not be empty!")
    private String id;

    @NotEmpty(message = "Username can not be empty!")
    @Size(min = 6, message = "Username must be more than \"5\" characters!")
    private String username;

    @NotEmpty(message = "Password can not be empty!")
    @Size(min = 7, message = "Password must be more than \"6\" characters long!")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain at least one letter and one digit.")
    private String password;

    @NotEmpty(message = "Email can not be empty!")
    @Email(message = "Email must be in valid email format!")
    private String email;

    @NotEmpty(message = "Role can not be empty!")
    @Pattern(regexp = "^(?i)(Admin|Customer)$", message = "Role must be either \"Admin\", or \"Customer\".")
    //(?i): make letters case-insensitive, customer = Customer
    private String role;

    @Positive(message = "Balance must be a positive number only!")
    private double balance;


}
