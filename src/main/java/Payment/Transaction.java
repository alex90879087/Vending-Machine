package Payment;

import App.VendingMachine;
import Roles.User;
import Snacks.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class Transaction {

    private int transactionId;
    private VendingMachine machine;
    private boolean complete;
    private boolean cancel = false;
    private User user;
    private Date date;
    private final double billAmount;
    private double paidAmount;
    private Payment payment; // cash or card
    private Cart cart;
    private String reason;
    private String typeStr;

    public enum Result {
        NOCHANGE, ENOUGH, NOTENOUGH, PAY, CARDVALID, CARDINVALID;
    }

    public Transaction(VendingMachine machine, User user, Cart cart) {
        this.transactionId = machine.getTransactionIdCount();
        this.machine = machine;
        this.user = user;
//        this.paymentID = paymentID;
        this.date = Calendar.getInstance().getTime();
        this.billAmount = 0;
        this.paidAmount = 0;
        this.complete = false;
        this.payment = null;
        this.typeStr = null;
        this.cart = cart;
    }

    public String getPaymentMethod() {
        return this.typeStr;
    }
    public void payAsCash(){
        this.payment = new Cash(this.machine, this.cart);
        this.typeStr = "cash";
    }

    public void payAsCard() {
        this.payment = new Card(this.machine, this.cart);
        this.typeStr = "card";
    }

    public Payment getPayment() {
        return this.payment;
    }
    public Cart getCart() {
        return this.cart;
    }
    public Date getDate() {
        return this.date;
    }
    public boolean isCompleted() {
        return this.complete;
    }
    public boolean isCancel() {
        return this.cancel;
    }
    public int getTransactionId() {
        return this.transactionId;
    }


//    public String getTimeString() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        return dateFormat.format(this.date);
//    }

    public Result execute() {
        Result r = payment.getResult();
        if (this.payment instanceof Cash) {
            if (r == Result.NOCHANGE || r == Result.ENOUGH) {
                complete();
                // todo: invoice
            }
        }
        return r;
    }
    public void recordReason(String str) {
        this.reason = str;
    }

    public String getReason(){
        return this.reason;
    }
    public void cancel() {
        this.cancel = true;
        machine.setTransactionIdcount(); // update transaction id
        machine.recordFailTransaction(this);

        System.out.println("> reported transaction");
    }
    public void complete() {
        this.complete = true;
        user.addTransaction(this);
        machine.setTransactionIdcount(); // update transaction id
        machine.addTransaction(this);
        updateStock();
    }

    public void updateStock() {
        Iterator<Map.Entry<Snacks, Integer>> new_Iterator = this.cart.getList().entrySet().iterator();
        while (new_Iterator.hasNext()) {
            Map.Entry<Snacks, Integer> new_Map = new_Iterator.next();
            for (Snacks snack : machine.getSnacks()) {
                if (snack.equals(new_Map.getKey())) {
                    snack.setQuantity(snack.getQuantity() - new_Map.getValue());
                    snack.sold(new_Map.getValue());

                }


            }
        }
    }

}






//    ARCHIVE
    //    public void pay(Cash cash) {
//        if (this.getPaidTotal() - this.getSnackTotal() < 0) {
//            System.out.println("Not enough money!");
//            return;
//        }
//
//        int status = cash.coinChange(this.getPaidTotal() - this.getSnackTotal());
//        if (status == 0) {
//            System.out.println("No change!");
//            this.changeMap = new HashMap<>();
//        }
//        else if (status == 1) {
//            System.out.println("Successful with change");
//            this.changeMap = cash.changeMap;
//            System.out.println(printChange());
//        }
//        else if (status == 3) {
//            System.out.println("Not enough change...");
//        }
//
//    }

//    public double getPaidAmount() {
//        for (int i = 0; i < paidCashList.size(); i ++) {
//            paidAmount += paidCashList.get(i);
//        }
//        return paidAmount;
//    }
//
//    public double getBillAmount() {
//        return this.cart.getCartBillAmount();
//    }


//    public String snackName() {
//        String toReturn = "";
//        for (Snacks snack: snacks) {
//            toReturn += snack.getName() + " " + "$" + snack.getPrice() + "\n";
//        }
//        return toReturn;
//    }

//    public String printChange() {
//        String toReturn = "Change : \n";
//        for (Double key: changeMap.keySet()) {
//            toReturn += changeMap.get(key) + " * $ " + key + "\n";
//        }
//        return toReturn;
//    }

    // TODO: if completed, update stock

//    public static void main(String[] args) {
//
//        VendingMachine machine = new VendingMachine();
//
//        // create transaction item by giving snack list and money list
//        Snacks a = new Candies("choc", "11", 4.2);
//        Snacks s = new Candies("banana", "11", 3.2);
//        Snacks d = new Candies("coco", "11", 2.2);
//
//        Cart cart = new Cart();
//        cart.addSnacks(a);
//        cart.addSnacks(s);
//        cart.addSnacks(d);
//
//        Payment cash = new Cash(machine, 001, cart.getCartBillAmount());
//        Transaction t = new Transaction(new Anonymous(), 001, cart, cash);
//
//        List<Double> money = new ArrayList<>();
//        money.add(4.2);
//        money.add(3.2);
//        money.add(3.0);
//
//        Transaction asd = new Transaction(money,snackls);
//
//
//        System.out.println(asd.snackName());
//
//        System.out.println("paid: " + asd.getPaidTotal());
//        System.out.println("total: " + asd.getSnackTotal());
//
//        asd.pay(cash);
//    }


