package org.example.amazonwebsitecapstone1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.ApiResponse.ApiResponse;
import org.example.amazonwebsitecapstone1.Model.MerchantStock;
import org.example.amazonwebsitecapstone1.Service.MerchantStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/amazon-clone/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;


    //CRUD//

    @GetMapping("/get")
    public ResponseEntity getMerchantStocks() {

        ArrayList<MerchantStock> merchantStocks = merchantStockService.getMerchantStocks();
        return ResponseEntity.status(200).body(merchantStocks);
    }

    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        int result = merchantStockService.addMerchantStock(merchantStock);

        if (result == -1)
            return ResponseEntity.status(400).body(new ApiResponse("This product ID not found!"));

        if (result == -2)
            return ResponseEntity.status(400).body(new ApiResponse("This merchant ID not found!"));

        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isUpdated = merchantStockService.updateMerchantStock(id, merchantStock);

        if (isUpdated)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock updated successfully!"));


        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock with this ID not found!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable String id) {

        boolean isDeleted = merchantStockService.deleteMerchantStock(id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock deleted successfully!"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock with this ID not found!"));

    }

    //End CRUD//

    @PutMapping("/add-moreStocks/{product_id}/{merchant_id}/{additionalAmount}")
    public ResponseEntity addProductToStock(@PathVariable String product_id, @PathVariable String merchant_id, @PathVariable int additionalAmount) {

        int updateStatus = merchantStockService.addProductToStock(product_id, merchant_id, additionalAmount);

        return switch (updateStatus) {
            case 1 -> ResponseEntity.status(200).body(new ApiResponse("Stock updated with new amount successfully!"));
            case -1 -> ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));
            case -2 -> ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));
            case 0 -> ResponseEntity.status(400).body(new ApiResponse("Additional amount must be more than zero!"));
            default -> ResponseEntity.status(400).body(new ApiResponse("Not found!"));
        };
    }

    /////////////////


    @PutMapping("/buy-product/{user_id}/{product_id}/{merchant_id}")
    public ResponseEntity buyProduct(@PathVariable String user_id, @PathVariable String product_id, @PathVariable String merchant_id) {

        int purchasingStatus = merchantStockService.buyProduct(user_id, product_id, merchant_id);

        return switch (purchasingStatus) {
            case -1 -> ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));
            case -2 -> ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));
            case -3 -> ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));
            case -4 -> ResponseEntity.status(400).body(new ApiResponse("This product out of stock!"));
            case -5 ->
                    ResponseEntity.status(400).body(new ApiResponse("User doesn't have enough balance to purchasing!"));
            case 1 -> ResponseEntity.status(200).body(new ApiResponse("Purchasing operation done successfully!"));
            default -> ResponseEntity.status(400).body(new ApiResponse("Not found!"));
        };

    }

    //2. extra endpoint: Return a product that user purchased

    @PutMapping("/return-product/{user_id}/{product_id}/{merchant_id}")
    public ResponseEntity returnProduct(@PathVariable String user_id, @PathVariable String product_id, @PathVariable String merchant_id) {

        int returnStatus = merchantStockService.returnProduct(user_id, product_id, merchant_id);

        switch (returnStatus) {
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("User with this ID not found!"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("Product with this ID not found!"));
            case -3:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));

            case -4:
                return ResponseEntity.status(400).body(new ApiResponse("This product not purchased from user!"));

            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Return operation done successfully!"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Not found!"));
        }

    }


    //3. extra endpoint: Allow user to rate a specific merchant

    @PutMapping("/merchant-rating/{user_id}/{merchant_id}/{rating}")
    public ResponseEntity merchantRating(@PathVariable String user_id, @PathVariable String merchant_id, @PathVariable int rating) {


        int ratingResult = merchantStockService.merchantRating(user_id, merchant_id, rating);

       switch ((ratingResult)){

           case -2:
               return ResponseEntity.status(400).body(new ApiResponse("Invalid rating value! Must be in this range(0-5)."));
           case -3:
               return ResponseEntity.status(400).body(new ApiResponse("User not found!"));
           case -1:
               return ResponseEntity.status(400).body(new ApiResponse("Merchant not found!"));

           case -4:
               return ResponseEntity.status(400).body(new ApiResponse("User doesn't purchase from this merchant until now!"));

           case 1:
               return ResponseEntity.status(200).body(new ApiResponse("Rating created to this merchant successfully!"));

           default:
               return ResponseEntity.status(400).body(new ApiResponse("Not found!"));

       }
    }


} //End controller
