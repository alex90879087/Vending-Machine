package Payment;

import App.VendingMachine;
import Payment.Transaction.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class Cash implements Payment {

    public double getBillAmount() {
        return billAmount;
    }

    //    private int paymentID;
    private double billAmount;
    private double insertedValue;
    private LinkedHashMap<String, Integer> machineCash; // sort based on insert order
    private LinkedHashMap<String, Integer> insertedCash; //
    private LinkedHashMap<String, Integer> changeMap; // change from Machine to Client
    private Result result = Result.PAY;
    private boolean paused = false;


    public Cash(VendingMachine machine, Cart cart) {
        //        this.paymentID = machine.getPaymentIdCount();
        this.insertedValue = 0;
        this.billAmount = cart.getCartBillAmount();
        this.machineCash = machine.getCashInMachine();
        this.insertedCash = new LinkedHashMap<>();
        this.changeMap = new LinkedHashMap<>();
    }

    @Override
    public void pause() {
        this.paused = true;
    }
    @Override
    public void unpause() {
        this.paused = false;
    }
    @Override
    public void setResult(Result r) {
        this.result = r;
    }

    // insert one by one as you do in real world
    public void insertCash(String type) {
        if (paused) return;
        // for client record
        if (insertedCash.containsKey(type)) {
            this.insertedCash.put(type, this.insertedCash.get(type) + 1); // key exist
        } else {
            this.insertedCash.put(type, 1); // key doesn't exist
        }

        this.machineCash.put(type, this.machineCash.get(type) + 1); // update machine cash data

        // calculate the total amount inserted by the client
        this.insertedValue = Payment.doubleCalculation(Double.parseDouble(type), insertedValue, "add");
//        System.out.printf("inserted Cash: %f \n", insertedValue);

    }
    public void ejectCash(String type) {
        // eject everything, type = all
        if (type.equals("all")) {
            System.out.println("> eject all coins");
            insertedCash.clear();
            insertedValue = 0;
            return;
        }

        if (paused) return;

        // for client record
        if (!insertedCash.containsKey(type)) return;

        if (insertedCash.containsKey(type) && this.insertedCash.get(type) > 1) {
            this.insertedCash.put(type, this.insertedCash.get(type) - 1); // key exist
        } else {
            this.insertedCash.remove(type); // key doesn't exist
        }

        this.machineCash.put(type, this.machineCash.get(type) - 1); // update machine cash data

        // calculate the total amount inserted by the client
        this.insertedValue = Payment.doubleCalculation(insertedValue, Double.parseDouble(type), "subtract");
        System.out.printf("inserted Cash: %f \n", insertedValue);

        // TODO: decide what to do when insert amount > bill
    }

//-------------------------------------------------
    public double changeCalculator(double remainder) {

        List<String> alKeys = new ArrayList<>(machineCash.keySet());
        Collections.reverse(alKeys);

        // reversed order -> largest possible
        for (String type : alKeys) {
            int number = machineCash.get(type);
            if (number > 0 && remainder > 0) {
                if (Payment.doubleCompare(Double.parseDouble(type), remainder) == -1 || Payment.doubleCompare(Double.parseDouble(type), remainder) == 0) {
                    // matched for change !!
                    // latest remainder
                    remainder = Payment.doubleCalculation(remainder, Double.parseDouble(type), "subtract");
                    this.machineCash.put(type, this.machineCash.get(type) - 1); // update machine cash value

                    // a map contains all coins change details for client.
                    if (changeMap.containsKey(type)) this.changeMap.put(type, this.changeMap.get(type) + 1); // key exist
                    else this.changeMap.put(type, 1); // key doesn't exist
                    if (remainder > 0) return changeCalculator(remainder); // keep going for the change
                }
            }
        }
        return remainder;
    }

    @Override
    public Result getResult() {
        double result;
        double remainder = Payment.doubleCalculation(insertedValue, billAmount, "subtract");
        int compareResult = Payment.doubleCompare(insertedValue, billAmount);

        if (remainder == 0) setResult(Result.NOCHANGE);
//        if (remainder == 0) setResult(Result.NOCHANGE);

        else if (remainder > 0) {
            result = changeCalculator(remainder); // change calculation
            if (result > 0) {
                setResult(Result.NOTENOUGH);
                this.changeMap.clear();
                System.out.printf("change map cleared: %s\n", changeMap);
            } else setResult(Result.ENOUGH);
        }
        else setResult(Result.PAY);
        System.out.println(this.result.toString());
        return this.result;
    }
    public double getInsertedValue() {
        return insertedValue;
    }
    public LinkedHashMap<String, Integer> getChangeMap() {
        return this.changeMap;
    }
}

