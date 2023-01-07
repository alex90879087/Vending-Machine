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

public class SnackTest {

    private VendingMachine machine;
    private Cart cart;
    private Customer user;
    private Transaction test;
    private static String dbPathSnacks = "src/main/resources/snacks.json";
    private static String dbPathUsers = "src/main/resources/users.json";
    private static String dbPathCash = "src/main/resources/CashTest.json";
    private String dbPathCard = "src/main/resources/CardTest.json";



    @BeforeEach
    void setUp() {
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
    }

    @Test
    void snackTest() {
    }
}
