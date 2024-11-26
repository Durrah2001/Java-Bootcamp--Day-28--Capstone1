package org.example.amazonwebsitecapstone1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.ApiResponse.ApiResponse;
import org.example.amazonwebsitecapstone1.Model.Merchant;
import org.example.amazonwebsitecapstone1.Service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/amazon-clone/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;


    //CRUD//

    @GetMapping("/get")
    public ResponseEntity getMerchant() {

        ArrayList<Merchant> merchants = merchantService.getMerchants();
        return ResponseEntity.status(200).body(merchants);
    }

    @PostMapping("/add")
    public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());


        merchantService.addMerchant(merchant);

        return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant(@PathVariable String id, @RequestBody @Valid Merchant merchant, Errors errors) {

        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        boolean isUpdated = merchantService.updateMerchant(id, merchant);

        if (isUpdated)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully!"));


        return ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchant(@PathVariable String id) {

        boolean isDeleted = merchantService.deleteMerchant(id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully!"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));

    }

    //End CRUD//

    //3. extra endpoint: Allow user to rate a specific merchant

    @PutMapping("/merchant-rating/{merchant_id}/{rating}")
    public ResponseEntity merchantRating(@PathVariable String merchant_id, @PathVariable int rating) {


        int ratingResult = merchantService.merchantRating(merchant_id, rating);

        if (ratingResult == 1)
            return ResponseEntity.status(200).body(new ApiResponse("This merchant rated successfully!"));

        if (ratingResult == -1)
            return ResponseEntity.status(400).body(new ApiResponse("Merchant with this ID not found!"));


        return ResponseEntity.status(400).body(new ApiResponse("Rating value must be in this range (0-5) only!"));
    }


} //End controller
