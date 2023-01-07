package Payment;

import Snacks.Snacks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
//    private List<Snacks> cart;
    private LinkedHashMap<Snacks, Integer> cart; // snack, the product; integer, number of this product
    private double totalAmount;

    public Cart() {
//        this.cart = new ArrayList<>();
        this.cart = new LinkedHashMap<>();
        this.totalAmount = 0;
    }

    public void addSnacks(Snacks snack) {
        if (snack == null) {
            System.out.println("snack cannot be null");
            return;
        }

        if (cart.containsKey(snack)) {
            this.cart.put(snack, this.cart.get(snack) + 1);
        } else {
            this.cart.put(snack, 1);
        }

        // calculation of billAmount
        BigDecimal totalAmountBD = new BigDecimal(totalAmount);
        BigDecimal snackPriceBD = new BigDecimal(snack.getPrice());
        BigDecimal result = totalAmountBD.add(snackPriceBD);
        result = result.setScale(3, RoundingMode.HALF_UP);
        this.totalAmount = result.doubleValue();
    }

    public LinkedHashMap<Snacks, Integer> getList() {
        return this.cart;
    }
    public void removeSnacks(Snacks snack) {
        if (cart.containsKey(snack) && this.cart.get(snack) > 0) {
            this.cart.put(snack, this.cart.get(snack) - 1);
        }
//        else if (cart.containsKey(snack) && this.cart.get(snack) == 0) {
//            this.cart.remove(snack);
//        }
        if (cart.containsKey(snack)) {
            // calculation of billAmount
            BigDecimal totalAmountBD = new BigDecimal(totalAmount);
            BigDecimal snackPriceBD = new BigDecimal(snack.getPrice());
            BigDecimal result = totalAmountBD.subtract(snackPriceBD);
            result = result.setScale(3, RoundingMode.HALF_UP);
            this.totalAmount = result.doubleValue();
        }
    }

    public void clearZero() {
        Iterator<Map.Entry<Snacks, Integer>> new_Iterator = this.cart.entrySet().iterator();
        while (new_Iterator.hasNext()) {
            Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
            if ((new_Map).getValue() == 0)
                new_Iterator.remove();
        }
    }

//    public List<Snacks> getCart() {
//        return this.cart;
//    }

    public double getCartBillAmount() {
        return this.totalAmount;
    }

}
