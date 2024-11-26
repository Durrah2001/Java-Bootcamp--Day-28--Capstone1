package org.example.amazonwebsitecapstone1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.ApiResponse.ApiResponse;
import org.example.amazonwebsitecapstone1.Model.User;
import org.example.amazonwebsitecapstone1.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/amazon-clone/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //CRUD//

    @GetMapping("/get")
    public ResponseEntity getUsers() {

        ArrayList<User> users = userService.getUsers();
        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());


        userService.addUser(user);

        return ResponseEntity.status(200).body(new ApiResponse("User added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody @Valid User user, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isUpdated = userService.updateUser(id, user);

        if (isUpdated)
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully!"));


        return ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {

        boolean isDeleted = userService.deleteUser(id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully!"));

        return ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));

    }

    //End CRUD//

    //1. extra endpoint: Apply a discount amount on specific product

    @PutMapping("/discount/{user_id}/{product_id}/{discount_amount}")
    public ResponseEntity discountOfProduct(@PathVariable String user_id, @PathVariable String product_id, @PathVariable double discount_amount) {

        int result = userService.discountOfProduct(user_id, product_id, discount_amount);

        switch (result) {
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("This discount amount not valid!"));

            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));

            case -3:
                return ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));

            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully for this product!"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Not found!"));

        }

    }

    //4. extra endpoint: Apply a discount amount on specific category products by an admin only

    @PutMapping("/discount-byCategory/{user_id}/{category_id}/{discount_amount}")
    public ResponseEntity discountByCategory(@PathVariable String user_id, @PathVariable String category_id, @PathVariable double discount_amount) {

        int discountStatus = userService.discountByCategory(user_id, category_id, discount_amount);

        switch (discountStatus) {

            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("User's role must be Admin to apply discount on specific category!"));

            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully for products in category: " + category_id));

            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("No product found in category: " + category_id));

            case -3:
                return ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));

            case -4:
                return ResponseEntity.status(400).body(new ApiResponse("Category with this ID not found!"));

            default:
                return ResponseEntity.status(400).body(new ApiResponse("Not found!"));

        }


    }


} //End controller
