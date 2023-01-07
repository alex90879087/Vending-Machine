package Snacks;

public class Chips implements Snacks{
    private String name;
    private double price;
    private int quantity;
    private String code;
    private int sold;

    public Chips(String name, String code, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 7;
        this.code = code;
        this.sold = 0;

    }

    public Chips(String name, String code, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.code = code;
        this.sold = 0;

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void sold(int qty) {
        this.sold += qty;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public int getSold() {
        return this.sold;
    }
}
