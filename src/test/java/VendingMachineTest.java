import App.VendingMachine;
import Payment.Cart;
import Payment.Cash;
import Payment.Transaction;
import Roles.Anonymous;
import Roles.Customer;
import Roles.Owner;
import Snacks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static Payment.Transaction.Result.NOCHANGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VendingMachineTest {

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

    public static void main(String[] args) {
        App test = new App();
        VendingMachine ve = App.machine;

        test.loadSnacks(dbPathSnacks); // load snacks database
        test.loadUsers(dbPathUsers); // load users database
        test.loadCash(dbPathCash); // load cash details : notes / coins

        System.out.println(1);
        System.out.println(ve.getCashInMachine());


//        System.out.println(test.getMachine().getSnacks());
//        for (Snacks snack: test.getMachine().getSnacks()) {
//            System.out.println(snack.getName());
//        }
    }

    @Test
    void testLogin() {

        assertEquals(Owner.class, machine.login("owner", "owner").getClass());
        machine.login("owner", "owner");

        assertEquals(null, machine.login("nonExist", "nonExist"));

        assertEquals(machine.getUser(), machine.login("owner", "owner"));

    }

    @Test
    void testGetSnacks() {
        List<String> expected = Arrays.asList("Mineral Water", "Sprite", "Coca cola", "Pepsi",
                "Juice", "Smiths", "Pringles", "Kettle", "Thins", "Mars","M&M", "Bounty",
                "Snickers", "Mentos", "Sour Patch", "Skittles");

        for (int i = 0; i < expected.size(); i ++) {
            assertEquals(expected.get(i), machine.getSnacks().get(i).getName());
        }
    }

    @Test
    void testGetUsers() {
        Customer a = new Customer("alex", "alex");
        Customer b = new Customer("ken", "ken");

        this.machine.addUser(a);

//        assertEquals(a.getUsername(), ((Customer) (this.machine.getUsers().get(1))).getUsername());

        this.machine.addUser(b);

        assertEquals(b.getUsername(), ((Customer) (this.machine.getUsers().get(this.machine.getUsers().size() - 1))).getUsername());
    }


    // test cash amount when machine starts and after a few transactions
    @Test
    void testAddSnack() {

        LinkedHashMap<String, Integer> testCash = new LinkedHashMap<>();
        testCash.put("0.05", 0);
        testCash.put("0.1", 10);
        testCash.put("0.2", 10);
        testCash.put("0.5", 10);
        testCash.put("1", 10);
        testCash.put("2", 10);
        testCash.put("5", 10);
        testCash.put("10", 10);
        testCash.put("20", 10);
        testCash.put("50", 1);

        assertEquals(testCash, machine.getCashInMachine());

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Drinks("p2", "01", 1 , 3);
        Snacks p3 = new Drinks("p3", "02", 1 , 3);

        machine.addSnacks(p1);
        machine.addSnacks(p2);
        machine.addSnacks(p3);

        assertEquals(3, machine.getSnacks().get(machine.getSnacks().size() - 1).getQuantity());

        assertEquals("p3", machine.getSnacks().get(machine.getSnacks().size() - 1).getName());

        assertEquals("02", machine.getSnacks().get(machine.getSnacks().size() - 1).getCode());

    }

    @Test
    void testCart() {


        machine.loginAnonymous();

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Drinks("p2", "01", 1 , 3);
        Snacks p3 = new Drinks("p3", "02", 1 , 3);

        machine.addSnacks(p1);
        machine.addSnacks(p2);
        machine.addSnacks(p3);

        machine.newCart();

        machine.getCart().addSnacks(p1);
        machine.getCart().addSnacks(p2);
        machine.getCart().addSnacks(p3);

        Cash cash = new Cash(machine, cart);

        System.out.println(cash.getBillAmount());

        cash.insertCash("2");
        cash.insertCash("2");

        assertEquals(NOCHANGE, cash.getResult());

        // log out and see if the cart is renewed
        machine.logout();

        assertEquals(0, machine.getCart().getList().size());

        assertEquals(0, machine.getCart().getCartBillAmount());

    }

    @Test
    void testRemoveUser() {
        machine.addUser(new Anonymous());

        assertEquals(Anonymous.class, machine.getUsers().get(machine.getUsers().size() - 1).getClass());

        Owner temp = new Owner("ownerTest", "ownerTest" );

        assertEquals(false, machine.removeUser(new Owner("nonExist", "nonExist")));
        machine.addUser(temp);

        machine.removeUser(temp);

        assertEquals(Anonymous.class, machine.getUsers().get(machine.getUsers().size() - 1).getClass());
    }


}