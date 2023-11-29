import factories.CombinationFactory;
import factories.RowFactory;

import java.util.ArrayList;
import java.util.List;

import static utils.VectorUtils.*;
import static utils.BinomialUtils.*;

public class ReedMuller {
    private final int m;
    private final int r;
    private final RowFactory rowFactory;
    private final CombinationFactory combinationFactory;
    private final List<int[]> codeGeneratingMatrix = new ArrayList<>();
    private final List<List<int[]>> votingRows = new ArrayList<>();
    private final List<Integer> lastIndexByOrder = new ArrayList<>();

    public int wordLength() {
        return codeGeneratingMatrix.size();
    }

    public int blockLength() {
        return (int) Math.pow(2, m);
    }

    public ReedMuller(int m, int r) {
        this.m = m;
        this.r = r;
        this.rowFactory = new RowFactory(m, blockLength());
        this.combinationFactory = new CombinationFactory(m);
        constructCodeGeneratingMatrix();
    }

    /**
     * A helper function that constructs the code generating matrix, the voting rows used in the decoding method and a list
     * that contains the indexes of the last row in the code generating matrix according to level.
     */
    private void constructCodeGeneratingMatrix() {
        List<int[]> firstOrderRows = new ArrayList<>();
        lastIndexByOrder.add(0);

        for (int coordinate = 0; coordinate < m; coordinate++) {
            firstOrderRows.add(rowFactory.createFirstOrderRow(coordinate));
        }

        for (int order = 0; order <= r; order++) {
            List<List<Integer>> rowCombinations = combinationFactory.createRowCombinations(order);

            for (List<Integer> rowCombination : rowCombinations) {
                List<Integer> complementCombination = combinationFactory.createComplementCombination(rowCombination);

                codeGeneratingMatrix.add(rowFactory.createOtherOrderRow(rowCombination, firstOrderRows));
                votingRows.add(rowFactory.createVotingRows(complementCombination, order));
            }

            // Create a list that contains the last index for each order. This is used in the decoding for easier access
            if (order != 0) {
                lastIndexByOrder.add(lastIndexByOrder.get(order - 1) + calculateBinomialCoefficient(m, order));
            }
        }
    }

    /**
     * A function that encodes a word according to the Reed-Muller code encoding method
     * @param word Word to encode
     * @return The encoded word
     */
    public int[] encode(int[] word) {
        if (word.length != wordLength()) {
            throw new IllegalArgumentException("Input word length does not match the Reed-Muller code message length.");
        }

        int[] encodedWord = new int[blockLength()];

        for (int i = 0; i < wordLength(); i++) {
            if (word[i] == 1) {
                encodedWord = vectorReduction(vectorAddition(encodedWord, codeGeneratingMatrix.get(i)), 2);
            }
        }

        return encodedWord;
    }

    /**
     * A function that decodes a word according to the Reed-Muller code decoding method
     * @param encodedWord An encoded word to decode
     * @return The decoded word
     */
    public int[] decode(int[] encodedWord) {
        int[] decodedWord = new int[wordLength()];

        for (int order = r; order >= 0; order--) {
            int[] rowSum = new int[blockLength()];
            int lastIndex = lastIndexByOrder.get(order);
            int firstIndex = (order == 0) ? 0 : (lastIndexByOrder.get(order - 1) + 1);

            for (int i = firstIndex; i <= lastIndex; i++) {
                int[] votes = new int[2];

                for (int[] votingRow : votingRows.get(i)) {
                    int vote = vectorDotProduct(encodedWord, votingRow) % 2;

                    votes[vote]++;
                }

                if (votes[1] > votes[0]) {
                    decodedWord[i] = 1;
                } else {
                    decodedWord[i] = 0;
                }

                if (decodedWord[i] == 1) {
                    rowSum = vectorReduction(vectorAddition(rowSum, codeGeneratingMatrix.get(i)), 2);
                }
            }

            encodedWord = vectorReduction(vectorAddition(encodedWord, rowSum), 2);
        }

        return decodedWord;
    }
}
