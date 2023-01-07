package Snacks;

public class CandiesFactory implements SnacksFactory{

    @Override
    public Snacks create(String name, String code, int quantity, double price) {
        return new Candies(name, code, price, quantity);
    }
}
