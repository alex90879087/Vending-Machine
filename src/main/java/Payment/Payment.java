package Payment;
import Payment.Transaction.Result;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Payment {

    void pause();
    void unpause();
    void setResult(Result r);

    Result getResult();
    static int doubleCompare(double value1, double value2) {
        BigDecimal value1BD = new BigDecimal(value1);
        BigDecimal value2BD = new BigDecimal(value2);
        value1BD.setScale(2, RoundingMode.HALF_UP);
        value2BD.setScale(2, RoundingMode.HALF_UP);
//        System.out.println(value2BD);
        int result = value1BD.compareTo(value2BD);
        return result;
    }
    static double doubleCalculation(double x, double y, String operator) {
        BigDecimal result;
        BigDecimal xBD = new BigDecimal(x);
        BigDecimal yBD = new BigDecimal(y);
        if (operator.equals("add")) {
            result = xBD.add(yBD);
        } else {
            result = xBD.subtract(yBD);
        }
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
