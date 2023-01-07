package Snacks;

public interface Snacks {

    // Seller can fill / modify the details:

    void setName(String name);
    void setCode(String code);
    void setQuantity(int quantity); // up to 15
    void setPrice(double price);
    void sold(int qty);
//    void setSold();


    String getName();
    String getCode();
    int getQuantity();
    double getPrice();
    int getSold();



}
