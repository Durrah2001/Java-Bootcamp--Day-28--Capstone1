package org.example.amazonwebsitecapstone1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.ApiResponse.ApiResponse;
import org.example.amazonwebsitecapstone1.Model.Product;
import org.example.amazonwebsitecapstone1.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/amazon-clone/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    //CRUD//

    @GetMapping("/get")
    public ResponseEntity getProducts() {

        ArrayList<Product> products = productService.getProducts();
        return ResponseEntity.status(200).body(products);
    }

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isCategoryIdExist = productService.addProduct(product);

        if (!isCategoryIdExist) {
            return ResponseEntity.status(400).body(new ApiResponse("Category with this ID not found!"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody @Valid Product product, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isUpdated = productService.updateProduct(id, product);

        if (isUpdated)
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully!"));


        return ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable String id) {

        boolean isDeleted = productService.deleteProduct(id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully!"));

        return ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));

    }

    //End CRUD//

    //5. extra: User can search for a product with specific price range
    @GetMapping("/products-byPrice/{minPrice}/{maxPrice}")
    public ResponseEntity findByPriceRange(@PathVariable double minPrice, @PathVariable double maxPrice) {

        ArrayList<Product> products = productService.findByPriceRange(minPrice, maxPrice);

        return ResponseEntity.status(200).body(products);


    }


} //End controller
