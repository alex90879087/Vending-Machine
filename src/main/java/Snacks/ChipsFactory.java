package Snacks;

public class ChipsFactory implements SnacksFactory{

    @Override
    public Snacks create(String name, String code, int quantity, double price) {
        return new Chips(name, code, price, quantity);
    }
}
