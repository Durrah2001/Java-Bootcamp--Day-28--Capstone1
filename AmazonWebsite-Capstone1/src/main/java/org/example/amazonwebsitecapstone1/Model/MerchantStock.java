package org.example.amazonwebsitecapstone1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "Merchant stock's ID can not be empty!")
    private String id;

    @NotEmpty(message = "Product ID can not be empty!")
    private String product_id;

    @NotEmpty(message = "Merchant ID can not be empty!")
    private String merchant_id;

    @Positive(message = "Stock must be a positive number!")
    @Min(value = 11, message = "Stock must be \"11\" at least!")
    private int stock;


}
