package utils;

public class BinomialUtils {

    /**
     * A recursive function that calculates the Binomial Coefficient according to the equation
     * C(n, k) = C(n-1, k-1) + C(n-1, k)
     * @param n number of objects to choose from
     * @param k number of objects to choose
     * @return The Binomial Coefficient
     */
    public static int calculateBinomialCoefficient(int n, int k) {
        if (k == 0 || k == n) {
            return 1;
        } else {
            return calculateBinomialCoefficient(n - 1, k - 1) + calculateBinomialCoefficient(n - 1, k);
        }
    }
}
