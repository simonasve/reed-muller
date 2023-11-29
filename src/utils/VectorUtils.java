package utils;

public class VectorUtils {

    /**
     * A function that takes two vectors and returns their dot product
     * @param x First vector to consider
     * @param y Second vector to consider
     * @return The dot product of the two provided vectors
     */
    public static int vectorDotProduct(int[] x, int[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Vector lengths do not match for dot product.");
        }

        int result = 0;

        for (int i = 0; i < x.length; i++) {
            result += x[i] * y[i];
        }

        return result;
    }

    /**
     * A function that takes two vectors multiplies them
     * @param x First vector to consider
     * @param y Second vector to consider
     * @return The resulting vector after multiplication
     */
    public static int[] vectorMultiplication(int[] x, int[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Vector lengths do not match for vector multiplication.");
        }

        int[] result = new int[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] * y[i];
        }

        return result;
    }

    /**
     * A function that takes two vectors adds them
     * @param x First vector to consider
     * @param y Second vector to consider
     * @return The resulting vector after addition
     */
    public static int[] vectorAddition(int[] x, int[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Vector lengths do not match for vector addition.");
        }

        int[] result = new int[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] + y[i];
        }

        return result;
    }

    /**
     * A function that takes a vector and a modulus value and reduces the vector
     * @param x Vector to reduce
     * @param modulus Modulus value
     * @return The resulting vector after reduction with the provided modulus value
     */
    public static int[] vectorReduction(int[] x, int modulus) {
        int[] result = new int[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] % modulus;
        }

        return result;
    }
}
