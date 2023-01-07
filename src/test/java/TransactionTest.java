import Payment.*;
import Roles.User;
import Snacks.Snacks;
import javafx.css.CssMetaData;
import org.junit.jupiter.api.BeforeEach;

import App.VendingMachine;
import Payment.Cart;
import Roles.Customer;
import Snacks.Candies;
import Snacks.Snacks;
import Snacks.Drinks;
import Snacks.Chocolates;
import Snacks.Chips;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import static Payment.Transaction.Result.*;
import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {

    private VendingMachine machine;
    private Cart cart;
    private Customer user;
    private Transaction test;
    private static String dbPathSnacks = "src/main/resources/snacks.json";
    private static String dbPathUsers = "src/main/resources/users.json";
    private static String dbPathCash = "src/main/resources/CashTest.json";
    private String dbPathCard = "src/main/resources/CardTest.json";

    @BeforeEach
    void setup() {

        App temp = new App();

        user = new Customer("Charles", "40691");

        temp.loadSnacks(dbPathSnacks); // load snacks database
        temp.loadUsers(dbPathUsers); // load users database
        temp.loadCash(dbPathCash); // load cash details : notes / coins
        temp.loadCards(dbPathCard);

        this.machine = App.machine;

        cart = new Cart();

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Candies("p2", "01", 1 , 3);
        Snacks p3 = new Chocolates("p3", "02", 1 , 3);
        Snacks p4 = new Chips("p4", "03", 1, 3);

        machine.addSnacks(p1);
        machine.addSnacks(p2);
        machine.addSnacks(p3);
        machine.addSnacks(p4);

        cart.addSnacks(p1);
        cart.addSnacks(p2);
        cart.addSnacks(p3);
        cart.addSnacks(p4);

        test = new Transaction(machine, user, cart);
    }

    @Test
    void testCompleteTraction(){

        machine.loginAnonymous();

        assertEquals(false, test.isCancel());

        test.payAsCard();

        boolean result = Card.cardValidation(machine.getCardsData(), user.getUsername(), Payment.tryParse(user.getPassword()));

        assertEquals(true, result);
        test.getPayment().pause();
        test.getPayment().setResult(CARDVALID);
        test.getPayment().unpause();
        assertEquals(CARDVALID, test.getPayment().getResult());

        Calendar today = Calendar.getInstance();
        assertEquals(today.getTime().getYear(), test.getDate().getYear());
        assertEquals(today.getTime().getMonth(), test.getDate().getMonth());
        assertEquals(today.getTime().getDay(), test.getDate().getDay());

        test.complete();

        Transaction test1 = new Transaction(machine, user, new Cart());

        assertEquals(101, test1.getTransactionId());

        assertEquals(true, test.isCompleted());

        machine.getUser().addTransaction(test);

        for (int i = 1; i < 5; i ++ ){
            String itemName = "p" + (5 - i);
            System.out.println(itemName);
            assertEquals(itemName, machine.getUser().getRecentItems().get(i - 1).getName());
        }



        for (Snacks snack: test.getCart().getList().keySet()) {
            snack.setCode("56");
            assertEquals("56", snack.getCode());
            int temp = snack.getQuantity();
            snack.setQuantity(snack.getQuantity());
            assertEquals(temp, snack.getQuantity());
            snack.setName("Test");
            assertEquals("Test", snack.getName());
            double price = snack.getPrice();
            snack.setPrice(price);
            assertEquals(price, snack.getPrice());
            assertEquals(2, snack.getQuantity());
            assertEquals(1, snack.getPrice());
        }

    }

    @Test
    void testCanceledTransaction() {

        test.payAsCash();

        boolean result = Card.cardValidation(machine.getCardsData(), user.getUsername(), 1 + Integer.parseInt(user.getPassword()));

        assertEquals(false, result);

        ((Cash) test.getPayment()).ejectCash("all");

        test.cancel();
        test.recordReason("change not available");

        assertEquals(true, test.isCancel());

        assertEquals(102, (new Transaction(machine, user, new Cart())).getTransactionId());
    }

    @Test
    void testTransaction() {


        test.payAsCash();

        Cash payment = (Cash) test.getPayment();

        payment.insertCash("20");

        System.out.println(payment.getResult());

        test.execute();

        System.out.println(test.getCart().getCartBillAmount());

        assertEquals(true, test.isCompleted());
    }
}
