package Payment;

import App.VendingMachine;
import Payment.Transaction.Result;
import Roles.Register;
import Roles.User;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Card implements Payment{

    private LinkedHashMap<String, Integer> cardsData;
    private double billAmount;
    private Result result = Result.PAY;
    private boolean paused = false;
    private boolean valid = false;

    public Card(VendingMachine machine, Cart cart) {
        cardsData = machine.getCardsData();
        this.billAmount = cart.getCartBillAmount();
    }

    public void isValid() {
        this.valid = true;
    }

    public void isInvalid() {
        this.valid = true;
    }

    public boolean checkCard() {
        return this.valid;
    }
    // or return int to for actions
    public static boolean cardValidation(HashMap<String, Integer> cardsData, String username, int number){
        if (cardsData.containsKey(username)) {
            return (cardsData.get(username) == number);
        }
        else{
            System.out.println("> Invalid Card Details Input");
            return false;
        }

    }

    // NOTE !! not to the database for matching, but only save as a reference in user.
//    public void addCard(String username, int number) {
//        if (!cardsData.containsKey(username)) {
//            // probably show a prompt
//            cardsData.put(username, number);
//            System.out.println("card added");
//        }
//        else System.out.println("Payment.Card already registered");
//    }

    public void saveCard(User user, String name, int number) {
        SavedCard newCard = new SavedCard(name, number);
        ((Register) user).saveCard(newCard);

    }
//    public void print() {
//        for (String key: cards.keySet()) {
//            System.out.println(key + " " + cards.get(key));
//        }
//    }

    // call this when users click the exit button


    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void unpause() {
        this.paused = false;
    }

    @Override
    public void setResult(Result r) {
        this.result = r;
    }

    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    public LinkedHashMap<String, Integer> getCardsData() {
        return cardsData;
    }
}
