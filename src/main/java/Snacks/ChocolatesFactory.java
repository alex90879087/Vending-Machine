package Snacks;

public class ChocolatesFactory implements SnacksFactory{

    @Override
    public Snacks create(String name, String code, int quantity, double price) {
        return new Chocolates(name, code, price, quantity);
    }
}
