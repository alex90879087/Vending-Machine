package App;

import Payment.Cart;
import Payment.Transaction;
import Roles.*;
import Snacks.*;

import java.util.*;


public class VendingMachine {

    private List<Snacks> snacks;
    private List<User> users;
    private List<Transaction> transactions;
    private List<Transaction> cancelTransactions;
    private LinkedHashMap<String, Integer> cashInMachine;
    private LinkedHashMap<String, Integer> cardsData;
    private SnacksFactoryRegistry reg;
    private long tickCount = 0;
//    private int paymentIdCount = 0;
    private int transactionIdCount = 100;
    private User user;
    private Cart cart;
    private boolean processingTransaction = false;

    /* Constructor */
    public VendingMachine() {
        this.snacks = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.cancelTransactions = new ArrayList<>();
        this.reg = new SnacksFactoryRegistry();
        this.users = new ArrayList<>();
        this.cashInMachine = new LinkedHashMap<>();
        this.cardsData = new LinkedHashMap<>();
        this.user = null;
        this.cart = new Cart();
        users.add(new Anonymous());
        reg.register("Drinks", new DrinksFactory());
        reg.register("Chips", new ChipsFactory());
        reg.register("Chocolates", new ChocolatesFactory());
        reg.register("Candies", new CandiesFactory());
    }

    public void processing() {
        this.processingTransaction = true;
    }
    public void idle() {
        this.processingTransaction = false;
    }
    public boolean isProcessing() {
        return this.processingTransaction;
    }

    public Cart getCart() {
        return this.cart;
    }
    public Cart newCart() {
        this.cart = new Cart();
        return this.cart;
    }

    private void loginUser(User user) {
        this.user = user;
    }

    public void loginAnonymous() {
        for (User user : users) {
            if (user instanceof Anonymous) {
                loginUser(user);
                System.out.println("> logged in as Anonymous User");
            }
        }
    }

    public void logout() {
        newCart();
        if (user != null) {
            // save, write everything to database
        }
        this.user = null;
    }
    public User getUser() {
        return this.user;
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (!(user instanceof Anonymous)) {
                if (((Register) user).getUsername().equals(username) && ((Register) user).getPassword().equals(password)) {
                    loginUser(user);
                    System.out.println("> User data matched");
                    return user;
                }
            }
        }
        return null;
    }

    public void setTransactionIdcount() {
        this.transactionIdCount++;
    }
    public void setCashInMachine(LinkedHashMap map) {
        this.cashInMachine = map;
    }
    public void setCardsDataBase(LinkedHashMap map) {
        this.cardsData = map;
    }
    public void addSnacks(Snacks snack){
        this.snacks.add(snack);
    }
    public void addUser(User user) {
        this.users.add(user);
    }
    public boolean checkUser(String username, String password) {
        for(User user : this.users) {
            if (user instanceof Register) {
                if (((Register) user).getUsername().equals(username) && ((Register) user).getPassword().equals(password)) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean removeUser(User user) {
        for(User data : this.users) {
            if (data.equals(user)) {
                this.users.remove(user);
                return true;
            }
        }
        return false;
    }
    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }
    public void recordFailTransaction(Transaction t) {
        this.cancelTransactions.add(t);
    }

    public int getTransactionIdCount() {
        return this.transactionIdCount;
    }
    public SnacksFactoryRegistry getFactoryRegistry() {
        return this.reg;
    }
    public List<User> getUsers() {
        return this.users;
    }
    public List<Snacks> getSnacks() {
        return this.snacks;
    }
    public List<Transaction> getTransactions() {
        Collections.sort(this.transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        return this.transactions;
    }

    public List<Transaction> getCancelTransactions() {
        Collections.sort(this.cancelTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return this.cancelTransactions;
    }
    public LinkedHashMap<String, Integer> getCashInMachine() {
        return this.cashInMachine;
    }
    public LinkedHashMap<String, Integer> getCardsData() {
        return this.cardsData;
    }
    public void tick() {
        tickCount++;
    }

    // Seller Specific



}
