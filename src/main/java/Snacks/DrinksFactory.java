package Snacks;

public class DrinksFactory implements SnacksFactory{

    @Override
    public Snacks create(String name, String code, int quantity, double price) {
        return new Drinks(name, code, price, quantity);
    }
}
