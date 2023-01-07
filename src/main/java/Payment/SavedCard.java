package Payment;

public class SavedCard {
    private String cardName;
    private int cardNumber;

    public SavedCard(String cardName, int cardNumber) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
    }

    public String getCardName(){
        return this.cardName;
    }

    public int getCardNumber() {
        return this.cardNumber;
    }
}
