package Roles;

import Payment.Transaction;
import Snacks.Snacks;

import java.util.List;

public interface User {

//    boolean login(String username, String password);

//    void setPassword(String password);

//    void addRecentItem(Snacks snack);
    void addTransaction(Transaction t);
    void sortTransaction(List<Transaction> t);     // sort transaction based on transaction date time

//    void loadRecentItems();

    List<Snacks> getRecentItems();




}
