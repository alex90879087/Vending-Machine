import App.VendingMachine;
import Payment.Card;
import Payment.Cart;
import Roles.Register;
import Roles.Seller;
import Roles.User;
import Snacks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static Payment.Transaction.Result.CARDINVALID;
import static Payment.Transaction.Result.CARDVALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class CardTest {

    private Cart cart;
    private Card card;
    private VendingMachine machine;

    private String dbPathSnacks = "src/main/resources/snacks.json";
    private String dbPathUsers = "src/main/resources/users.json";
    private String dbPathCash = "src/main/resources/CashTest.json";
    private String dbPathCard = "src/main/resources/CardTest.json";

    @BeforeEach
    void setUp() {
        cart = new Cart();

        Snacks p1 = new Drinks("p1", "00", 1 , 3);
        Snacks p2 = new Candies("p2", "01", 1 , 3);
        Snacks p3 = new Chocolates("p3", "02", 1 , 3);
        Snacks p4 = new Chips("p3", "02", 1 , 3);


        cart.addSnacks(p1);
        cart.addSnacks(p2);
        cart.addSnacks(p3);
        cart.addSnacks(p4);

        // load vending machine
        App app = new App();

        app.loadSnacks(dbPathSnacks); // load snacks database
        app.loadUsers(dbPathUsers); // load users database
        app.loadCash(dbPathCash); // load cash details : notes / coins
        app.loadCards(dbPathCard);

        this.machine = App.machine;

        card = new Card(machine, cart);
    }

    @Test
    void testCardData() {
        LinkedHashMap<String, Integer> testData = new LinkedHashMap<>();
        testData.put("Charles", 40691);
        testData.put("Sergio", 42689);
        testData.put("Kasey", 60146);
        testData.put("abc", 123);

        // should return true
        assertEquals(true, Card.cardValidation(machine.getCardsData(), "Charles", 40691));

        card.setResult(CARDVALID);
//        assertEquals(true, card.checkCard());
        // return false with wrong name
        assertEquals(false, Card.cardValidation(machine.getCardsData(), "Charles", 40692));
//        card.isInvalid();
//        assertEquals(false, card.checkCard());
        // return false with wrong card number
        assertEquals(false, Card.cardValidation(machine.getCardsData(), "charles", 40692));
    }

    @Test
    void testInvalidCard() {

        assertEquals(false, Card.cardValidation(machine.getCardsData(), "Charles", 40111));

        card.setResult(CARDINVALID);

        assertEquals(CARDINVALID, card.getResult());
    }

    @Test
    void testAddCard() {

        assertEquals(false, Card.cardValidation(card.getCardsData(), "a", 1));

        User user = new Seller("testing", "testing");
        Card card = new Card(machine, new Cart());
        card.saveCard(user, "NewCard", 13579);
        assertEquals("NewCard", ((Register) user).getSavedCard().getCardName());
        assertEquals(13579, ((Register) user).getSavedCard().getCardNumber());

    }


}