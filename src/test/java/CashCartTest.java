import Payment.Cart;
import Payment.Cash;
import Payment.Payment;
import Snacks.Snacks;
import App.VendingMachine;
import Snacks.Drinks;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Snacks.Candies;
import Snacks.Chips;
import Snacks.Chocolates;

import java.util.LinkedHashMap;

import static Payment.Transaction.Result.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CashCartTest {


    private Cart cart;
    private Cash cash;
    private VendingMachine machine;

    private String dbPathSnacks = "src/main/resources/snacks.json";
    private String dbPathUsers = "src/main/resources/users.json";
    private String dbPathCash = "src/main/resources/CashTest.json";

    @BeforeEach
    void setUp() {
        cart = new Cart();

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Candies("p2", "01", 1 , 3);
        Snacks p3 = new Chocolates("p3", "02", 1 , 3);

        cart.addSnacks(p1);
        cart.addSnacks(p2);
        cart.addSnacks(p3);


        // load vending machine
        App app = new App();

        app.loadSnacks(dbPathSnacks); // load snacks database
        app.loadUsers(dbPathUsers); // load users database
        app.loadCash(dbPathCash); // load cash details : notes / coins

        this.machine = App.machine;

        cash = new Cash(machine, cart);
    }


    @Test
    void testCart() {

        cart = new Cart();

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Candies("p2", "01", 1 , 3);
        Snacks p3 = new Chocolates("p3", "02", 1 , 3);
        Snacks p4 = new Chips("p3", "02", 1 , 3);

        cart.addSnacks(p1);
        cart.addSnacks(p2);
        cart.addSnacks(p3);

        assertEquals(3, cart.getCartBillAmount());

        cart.addSnacks(p1);
        assertEquals(4,cart.getCartBillAmount());

        assertEquals(2, cart.getList().get(p1));


        assertDoesNotThrow(() -> cart.addSnacks(null));
        assertDoesNotThrow(() -> cart.removeSnacks(null));
        assertEquals(0,  Payment.tryParse("A"));

        // test when removing non-existing snack
        cart.removeSnacks(p1);
        cart.removeSnacks(p1);

        assertEquals(2, cart.getCartBillAmount());
    }


    @Test
    void testCashWithNoChange() {

        cash.insertCash("1");
        cash.insertCash("1");
        cash.insertCash("1");

        assertEquals(3, cash.getInsertedValue());

        assertEquals(NOCHANGE, cash.getResult());
    }

    @Test
    void testCashWithEnoughChange() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        cash.insertCash("2");
        cash.insertCash("2");

        assertEquals(4, cash.getInsertedValue());

        assertEquals(ENOUGH, cash.getResult());

        map.put("1", 1);

        // should have 1 * 1 dollar in the map
        assertEquals(map, cash.getChangeMap());
    }

    @Test
    void testCashWithNotEnoughChange() {

        Snacks temp = new Drinks("p", "00", 0.05 , 3);
        cart.addSnacks(temp);
        cash = new Cash(this.machine, this.cart);

        assertEquals(3.05, cash.getBillAmount());

        cash.insertCash("2");
        cash.insertCash("2");

        assertEquals(4, cash.getInsertedValue());

        assertEquals(NOTENOUGH, cash.getResult());

    }

    @Test
    void testCashWithNotEnoughInsert() {

        Snacks temp = new Drinks("p", "00", 0.05 , 3);
        cart.addSnacks(temp);
        cash = new Cash(this.machine, this.cart);

        cash.insertCash("1");
        cash.insertCash("1");

        assertEquals(2, cash.getInsertedValue());

        assertEquals(PAY, cash.getResult());

        cash.insertCash("1");
        cash.insertCash("0.05");

        assertEquals(3.05, cash.getInsertedValue());

        assertEquals(NOCHANGE, cash.getResult());

    }

    @Test
    void testEjectMoney() {

        cash.insertCash("0.1");
        cash.insertCash("1");

        cash.ejectCash("1");

        assertEquals(0.1, cash.getInsertedValue());


        // test eject money that hasn't been inserted throws error or not
        assertDoesNotThrow(() -> cash.ejectCash("5"));

        cash.insertCash("1");
        cash.insertCash("1");

        assertEquals(2.1, cash.getInsertedValue());

        cash.ejectCash("1");

        assertEquals(1.1, cash.getInsertedValue() );


        cash.ejectCash("all");

        assertEquals(0, cash.getInsertedValue());

        // eject money twice should return 0 for inserted cash
        cash.ejectCash("all");

        assertEquals(0, cash.getInsertedValue());

    }



}