package Snacks;

public interface SnacksFactory {

    Snacks create(String name, String code, int quantity, double price);

}
