public class Channel {

    private final double probabilityOfError;

    public Channel(double probabilityOfError) {
        this.probabilityOfError = probabilityOfError;
    }

    /**
     * A function that might change the values of an array according to a given probability of error
     * @param word An array that might change values
     */
    public int[] sendTroughChannel(int[] word) {
        int[] result = new int[word.length];

        for (int i = 0; i < word.length; i++) {
            double probability = Math.random();

            if (probability < probabilityOfError) {
                if (word[i] == 0) {
                    result[i] = 1;
                } else {
                    result[i] = 0;
                }
            } else {
                result[i] = word[i];
            }
        }

        return result;
    }
}
