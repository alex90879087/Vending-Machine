package Roles;

import Payment.SavedCard;
import Payment.Transaction;
import Snacks.Snacks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class Register implements User{

    private String username;
    private String password;
    private List<Snacks> recentItems;
    private List<Transaction> transactions;
    private SavedCard savedCard;

    public Register(String username, String password) {
        this.username = username;
        this.password = password;
        this.recentItems = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.savedCard = null;
    }

    // TODO: check if given username exists in user.json
    public boolean checkUsername(String username) {
        if (this.username.equals(username)){
            return true;
        }
        return false;
    }

    // TODO: create a json file based on given username
    public static boolean createFile(String username) {
        try {
            // check if file exists
            String fileName = "src/resources/users" + username + ".json";
            File f = new File(fileName);
            if (f.exists() && !f.isDirectory()) {
                System.out.println("> User Database already exists");
                return false;
            }
            try {
                FileWriter file = new FileWriter(fileName);
                file.write("[]");
                file.close();
                System.out.printf("> JSON file created: %s.json\n", username);
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("> Invalid username");
        }
        return false;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return this.password;
    }
    public List<Snacks> getRecentItems() {
        return this.recentItems;
    }


    // TODO: update password
    public void setPassword(String password) {
        this.password = password;
    }

    public void saveCard(SavedCard card) {
        this.savedCard = card;
    }

    public SavedCard getSavedCard() {
        return this.savedCard;
    }


    private void loadRecentItems() {
        int count = 0;
        this.recentItems = new ArrayList<Snacks>();
        sortTransaction(this.transactions); // sort

        for (Transaction t : this.transactions) {
            if (t.isCompleted()) {
                // TODO: loop cart and reverse
                Set<Snacks> set = t.getCart().getList().keySet();
                List<Snacks> alKeys = new ArrayList<>(set);
                Collections.reverse(alKeys);

                // adding to [recentItems], 5 products now, not 5 items
                for (Snacks snack : alKeys) {
                    if (count > 5) break;
                    int qty = t.getCart().getList().get(snack);
                    if (qty > 0) this.recentItems.add(snack);
                    count++;
                }
            }
        }
    }
    @Override
    public void addTransaction(Transaction t){
        this.transactions.add(t);
        loadRecentItems();
    }
    @Override
    public void sortTransaction(List<Transaction> t) {
        Collections.sort(t, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }


}
