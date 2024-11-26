package org.example.amazonwebsitecapstone1.Service;

import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.Model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {


    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    private final ProductService productService;
    private final MerchantService merchantService;
    private final UserService userService;

    //CRUD//

    public ArrayList<MerchantStock> getMerchantStocks() {
        return merchantStocks;
    }

    public int addMerchantStock(MerchantStock merchantStock) {

        ArrayList<Product> products = productService.getProducts();
        ArrayList<Merchant> merchants = merchantService.getMerchants();

        boolean isProductExist = false;
        boolean isMerchantExist = false;

        for (Product p : products) {
            if (p.getId().equals(merchantStock.getProduct_id())) {
                isProductExist = true;
                break;
            }
        }

        for (Merchant m : merchants) {
            if (m.getId().equals(merchantStock.getMerchant_id())) {
                isMerchantExist = true;
                break;
            }
        }

        if (!isProductExist) {
            return -1;
        }

        if (!isMerchantExist) {
            return -2;
        }


        merchantStocks.add(merchantStock);
        return 1;
    }

    public boolean updateMerchantStock(String id, MerchantStock merchantStock) {

        for (int i = 0; i < merchantStocks.size(); i++) {

            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.set(i, merchantStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStock(String id) {

        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getId().equals(id)) {
                merchantStocks.remove(merchantStock);
                return true;
            }
        }

        return false;
    }

    //End CRUD//

    public int addProductToStock(String product_id, String merchant_id, int additionalAmount) {

        if (additionalAmount <= 0) { //Amount can't be less than or zero

            return 0;
        }

        for (MerchantStock merchantStock : merchantStocks) {

            if (merchantStock.getProduct_id().equals(product_id)) {

                if (merchantStock.getMerchant_id().equals(merchant_id)) {
                    merchantStock.setStock((merchantStock.getStock() + additionalAmount));

                    return 1; // if all conditions match

                }
                return -2; // if merchant_id not found

            }
        }

        return -1; // if product_id not found
    }
///////////////////////////


    public int buyProduct(String user_id, String product_id, String merchant_id) {

        ArrayList<User> users = userService.getUsers();
        ArrayList<Product> products = productService.getProducts();
        ArrayList<Merchant> merchants = merchantService.getMerchants();


        User user = null;
        for (User u : users) {
            if (u.getId().equals(user_id)) {
                user = u; //User is found
                break;
            }
        }

        if (user == null)
            return -1;


        Product product = null;
        for (Product p : products) {
            if (p.getId().equals(product_id)) {
                product = p;  //Product is found
                break;
            }
        }

        if (product == null)
            return -2;


        Merchant merchant = null;
        for (Merchant m : merchants) {
            if (m.getId().equals(merchant_id)) {
                merchant = m; //Merchant is found
                break;
            }
        }

        if (merchant == null)
            return -3;


        ////////////////////////////////////////////////////

        for (MerchantStock merchantStock : merchantStocks) {

            if (merchantStock.getProduct_id().equals(product_id) && merchantStock.getMerchant_id().equals(merchant_id))
                if (merchantStock.getStock() <= 0)
                    return -4;
        }

        for (MerchantStock m : merchantStocks) {
            m.setStock(m.getStock() - 1);
            //reduce the stock from the MerchantStock.
        }

        if (user.getBalance() < product.getPrice()) {
            return -5;
        }

        user.setBalance(user.getBalance() - product.getPrice());


        if (user.getPurchasedProducts() == null) {
            user.setPurchasedProducts(new ArrayList<>());
        }
        user.getPurchasedProducts().add(product_id);

        if (user.getPurchasedMerchants() == null) {
            user.setPurchasedMerchants(new ArrayList<>());
        }
        if (!user.getPurchasedMerchants().contains(merchant_id)) {
            user.getPurchasedMerchants().add(merchant_id); //add merchant to the list if not already added before
        }


        return 1;


    } //end buyProduct endpoint


    //2. extra endpoint: Return a product that user purchased

    public int returnProduct(String user_id, String product_id, String merchant_id) {

        ArrayList<User> users = userService.getUsers();
        ArrayList<Product> products = productService.getProducts();
        ArrayList<Merchant> merchants = merchantService.getMerchants();


        User user = null;
        for (User u : users) {
            if (u.getId().equals(user_id)) {
                user = u; //User is found
                break;
            }
        }
        if (user == null)
            return -1;

        Product product = null;
        for (Product p : products) {
            if (p.getId().equals(product_id)) {
                product = p;  //Product is found
                break;
            }
        }

        if (product == null)
            return -2;


        Merchant merchant = null;
        for (Merchant m : merchants) {
            if (m.getId().equals(merchant_id)) {
                merchant = m; //Merchant is found
                break;
            }
        }

        if (merchant == null)
            return -3;


        // check if the product was purchased by the user
        if (user.getPurchasedProducts() == null || !user.getPurchasedProducts().contains(product_id)) {
            return -4; // Product not purchased by user
        }

        user.setPurchasedMerchants(new ArrayList<>());

        if (!user.getPurchasedMerchants().contains(merchant_id)) {
            user.getPurchasedMerchants().add(merchant_id); // Add merchant to the list if not already added
        }


        for (MerchantStock merchantStock : merchantStocks) {

            if (merchantStock.getProduct_id().equals(product_id) && merchantStock.getMerchant_id().equals(merchant_id)) {
                merchantStock.setStock(merchantStock.getStock() + 1);
            }
        }


        double returnAmount = product.getPrice();
        user.setBalance(user.getBalance() + returnAmount); //return amount to user

        return 1;
    }

    //3. extra endpoint: Allow user to rate a specific merchant

    public int merchantRating(String user_id, String merchant_id, int rating) {

        ArrayList<User> users = userService.getUsers();
        ArrayList<Merchant> merchants = merchantService.getMerchants();

        if (rating < 0 || rating > 5) {
            return -2; // Invalid rating
        }

        User user = null;
        for (User u : users) {
            if (u.getId().equals(user_id)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            return -3; // User not found
        }

        Merchant merchant = null;
        for (Merchant m : merchants) {
            if (m.getId().equals(merchant_id)) {
                merchant = m;
                break;
            }
        }
        if (merchant == null) {
            return -1; // Merchant not found
        }

        // Check if the user has purchased from this merchant
        if (user.getPurchasedMerchants() == null || !user.getPurchasedMerchants().contains(merchant_id)) {
            return -4; // User has not purchased from this merchant
        }


        merchant.setNumOfRating(merchant.getNumOfRating() + 1);
        merchant.setRatingAvg(
                ((merchant.getRatingAvg() * (merchant.getNumOfRating() - 1)) + rating) / merchant.getNumOfRating()
        );

        return 1; // Success
    }



} //End service

