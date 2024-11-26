package org.example.amazonwebsitecapstone1.Service;

import lombok.RequiredArgsConstructor;
import org.example.amazonwebsitecapstone1.Model.Category;
import org.example.amazonwebsitecapstone1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {


    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    //CRUD//

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean addProduct(Product product) {

        ArrayList<Category> categories = categoryService.getCategories();

        boolean isExist = false;

        for (Category c : categories) {

            if (c.getId().equals(product.getCategory_id())) {
                isExist = true;
                break;
            }
        }

        if (!isExist)
            return false;  //cat id not found

        products.add(product);
        return true;


    }


    public boolean updateProduct(String id, Product product) {

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getId().equals(id)) {
                products.set(i, product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String id) {

        for (Product product : products) {
            if (product.getId().equals(id)) {
                products.remove(product);
                return true;
            }
        }

        return false;
    }

    //End CRUD//

    //5. extra: User can search for a product with specific price range

    public ArrayList<Product> findByPriceRange(double minPrice, double maxPrice) {
        ArrayList<Product> productsByPrice = new ArrayList<>();

        for (Product product : products) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                productsByPrice.add(product);
            }
        }

        return productsByPrice;
    }


} //End service

