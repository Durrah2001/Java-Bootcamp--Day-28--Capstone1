package org.example.amazonwebsitecapstone1.Service;

import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.Model.Category;
import org.example.amazonwebsitecapstone1.Model.Product;
import org.example.amazonwebsitecapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    ArrayList<User> users = new ArrayList<>();
    private final ProductService productService;
    private final CategoryService categoryService;

    //CRUD//

    public ArrayList<User> getUsers() {

        return users;
    }

    public void addUser(User user) {

        users.add(user);
    }

    public boolean updateUser(String id, User user) {

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getId().equals(id)) {
                users.set(i, user);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String id) {

        for (User u : users) {
            if (u.getId().equals(id)) {
                users.remove(u);
                return true;
            }
        }

        return false;
    }

    //End CRUD//

    //1. extra endpoint: Get a discount amount on specific product

    public int discountOfProduct(String user_id, String product_id, double discount_amount) {

        List<Double> allowedDiscounts = Arrays.asList(5.0, 10.0, 15.0, 20.0, 25.0);

        ArrayList<Product> products = productService.getProducts();
        if (discount_amount <= 0 || !allowedDiscounts.contains(discount_amount)) {
            return -1;
        }

        for (User u : users) {
            if (u.getId().equals(user_id)) {

                for (Product p : products) {

                    if (p.getId().equals(product_id)) {
                        double priceAfterDiscount = (p.getPrice() - (p.getPrice() * discount_amount / 100));
                        p.setPrice(priceAfterDiscount);
                        return 1;
                    }
                }
                return -2; //product not found
            }
        } //End for
        return -3;  //user not found
    }

    //4. extra endpoint: Apply a discount amount on specific category products by an admin only

    public int discountByCategory(String user_id, String category_id, double discount_amount) {

        ArrayList<Category> categories = categoryService.getCategories();
        Category category = null;
        for (Category c : categories) {
            if (c.getId().equals(category_id)) {
                category = c; //Category is found
                break;
            }
        }

        if (category == null)
            return -4;

        ArrayList<Product> products = productService.getProducts();
        for (User u : users) {
            if (u.getId().equals(user_id)) {

                if (!u.getRole().equalsIgnoreCase("Admin")) {
                    return -1;  //Must be an admin
                }

                boolean isProductFound = false; //in this category
                for (Product p : products) {

                    if (p.getCategory_id().equals(category_id)) {
                        isProductFound = true;


                        double discountAmount = p.getPrice() * (discount_amount / 100);
                        double priceAfterDiscount = p.getPrice() - discountAmount;

                        //update product's price
                        p.setPrice(priceAfterDiscount);

                    }
                }
                if (!isProductFound) {
                    return -2;
                }
                //product not found

                return 1;

            }

        } //End for
        return -3;  //user not found
    }


} //End service

