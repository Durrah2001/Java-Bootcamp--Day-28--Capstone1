package org.example.amazonwebsitecapstone1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.ApiResponse.ApiResponse;
import org.example.amazonwebsitecapstone1.Model.Category;
import org.example.amazonwebsitecapstone1.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/amazon-clone/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    //CRUD//

    @GetMapping("/get")
    public ResponseEntity getCategories() {

        ArrayList<Category> categories = categoryService.getCategories();
        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());


        categoryService.addCategory(category);

        return ResponseEntity.status(200).body(new ApiResponse("Category added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable String id, @RequestBody @Valid Category category, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isUpdated = categoryService.updateCategory(id, category);

        if (isUpdated)
            return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully!"));


        return ResponseEntity.status(400).body(new ApiResponse("Category with this ID not found!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable String id) {

        boolean isDeleted = categoryService.deleteCategory(id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully!"));

        return ResponseEntity.status(400).body(new ApiResponse("Category with this ID not found!"));

    }

    //End CRUD//


} //End controller
