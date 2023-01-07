package Snacks;

import java.util.HashMap;
import java.util.Map;

public class SnacksFactoryRegistry {
    private Map<String, SnacksFactory> registry;

    public SnacksFactoryRegistry() {
        this.registry = new HashMap<>();
    }

    public void register(String name, SnacksFactory factory) {
        registry.putIfAbsent(name, factory);
    }

    public Snacks create(String category, String name, String code, int quantity, double price) {
        SnacksFactory factory = registry.get(category);
        return factory.create(name, code, quantity, price);
    }

    // TODO: confirm modified types
    public Snacks modify(Snacks snacks, String newCategory) {
        SnacksFactory factory = registry.get(newCategory);
        String name = snacks.getName();
        int quantity = snacks.getQuantity();
        String code = snacks.getCode();
        double price = snacks.getPrice();
        return factory.create(name, code, quantity, price);
    }

}
