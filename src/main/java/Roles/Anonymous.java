package Roles;

import Payment.Transaction;
import Snacks.Snacks;

import java.util.*;

public class Anonymous implements User{

    private List<Snacks> recentItems; // a list to show 5 recent items

    // a recent transaction to fill up [recentItems]
    private List<Transaction> transactions;

    public Anonymous() {
        this.recentItems = new ArrayList<>();
        this.transactions = new ArrayList<>(); // TODO: first startup, load from db
    }

    @Override
    public List<Snacks> getRecentItems() {
        return this.recentItems;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        loadRecentItems();
    }

    // sort transaction based on transaction date time
    @Override
    public void sortTransaction(List<Transaction> t) {
        Collections.sort(t, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    private void loadRecentItems() {
        System.out.println("> saving recent items");
        this.recentItems = new ArrayList<Snacks>();
        int count = 0;
        sortTransaction(this.transactions); // sort

        for (Transaction t : this.transactions) {
            if (t.isCompleted() && count < 5) {
                // TODO: loop cart and reverse
                Set<Snacks> set = t.getCart().getList().keySet();
                List<Snacks> alKeys = new ArrayList<>(set);
                Collections.reverse(alKeys);

                // adding to [recentItems], 5 products now, not 5 items
                for (Snacks snack : alKeys) {
                    if (count > 5) break;
//                    int qty = t.getCart().getList().get(snack);
//                    if (qty > 0)
                    this.recentItems.add(snack);
                    count++;
                }
            }
        }
    }


}
