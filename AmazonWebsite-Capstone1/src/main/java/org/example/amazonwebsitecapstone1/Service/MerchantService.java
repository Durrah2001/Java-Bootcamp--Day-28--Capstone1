package org.example.amazonwebsitecapstone1.Service;

import org.example.amazonwebsitecapstone1.Model.Merchant;
import org.example.amazonwebsitecapstone1.Model.Product;
import org.example.amazonwebsitecapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {


    ArrayList<Merchant> merchants = new ArrayList<>();

    //CRUD//

    public ArrayList<Merchant> getMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public boolean updateMerchant(String id, Merchant merchant) {

        for (int i = 0; i < merchants.size(); i++) {

            if (merchants.get(i).getId().equals(id)) {
                merchants.set(i, merchant);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchant(String id) {

        for (Merchant m : merchants) {
            if (m.getId().equals(id)) {
                merchants.remove(m);
                return true;
            }
        }

        return false;
    }

    //End CRUD//

    //3. extra endpoint: Allow user to rate a specific merchant

    public int merchantRating(String merchant_id, int rating) {

        if (rating < 0 || rating > 5)
            return -2;


        for (Merchant m : merchants) {

            if (m.getId().equals(merchant_id)) {

                m.setNumOfRating(m.getNumOfRating() + 1);
                //Avg: -1 mean that the new rating will not be calculated
                m.setRatingAvg(((m.getRatingAvg() * (m.getNumOfRating() - 1)) + rating) / m.getNumOfRating());

                return 1;

            }
        }
        return -1;  //merchant not found
    }


} //End service
