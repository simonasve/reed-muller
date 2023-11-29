package factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.VectorUtils.vectorMultiplication;

public class RowFactory {
    private final int m;
    private final int blockLength;
    private final int[][] bitArrangements;

    public RowFactory(int m, int blockLength) {
        this.m = m;
        this.blockLength = blockLength;
        this.bitArrangements = createBitArrangements(m);
    }

    /**
     * A helper function that creates all possible bit arrangements up to the m-th bit
     * @param m Up to what number of bits to make arrangements
     * @return All possible bit arrangements
     */
    private int[][] createBitArrangements(int m) {
        int numOfArrangements = (int) Math.pow(2, m);
        int[][] bitArrangements = new int[numOfArrangements][m];

        for (int i = 0; i < numOfArrangements; i++) {
            for (int j = 0; j < m; j++) {
                bitArrangements[i][m - 1 - j] = (i >> j) & 1;
            }
        }

        return bitArrangements;
    }

    /**
     * A function used to create a first order row of the code generating matrix
     * @param coordinate Which bit to consider
     * @return The row according to the coordinate
     */
    public int[] createFirstOrderRow(int coordinate) {
        int[] result = new int[blockLength];

        for (int i = 0; i < blockLength; i++) {
            if (bitArrangements[i][coordinate] == 0) {
                result[i] = 1;
            }
        }

        return result;
    }

    /**
     * A function used to create lower or higher order rows based on provided combinations
     * @param combination The combination of row indexes to multiply
     * @param firstOrderRows The first order rows of the code generating matrix
     * @return The lower or higher order row
     */
    public int[] createOtherOrderRow(List<Integer> combination, List<int[]> firstOrderRows) {
        int[] result = new int[blockLength];
        Arrays.fill(result, 1);

        for (int member : combination) {
            int[] firstOrderRow = firstOrderRows.get(member);

            for (int i = 0; i < firstOrderRow.length; i++) {
                result = vectorMultiplication(result, firstOrderRow);
            }
        }

        return result;
    }

    /**
     * A function used to create voting rows for a given coordinate set that are used in decoding
     * @param coordinates A coordinate set to create the voting rows for
     * @param order The order of the coordinates
     * @return The resulting voting rows
     */
    public List<int[]> createVotingRows(List<Integer> coordinates, int order) {
        int[][] bitArrangementsByOrder = createBitArrangements(m - order);
        List<int[]> result = new ArrayList<>();

        for (int[] bitArrangementByOrder : bitArrangementsByOrder) {
            int[] resultRow = new int[blockLength];

            for (int i = 0; i < blockLength; i++) {
                boolean coordinatesAreEqual = true;

                for (int j = 0; j < coordinates.size(); j++) {
                    int coordinate = coordinates.get(j);
                    coordinatesAreEqual = coordinatesAreEqual && bitArrangements[i][coordinate] == bitArrangementByOrder[j];
                }

                if (coordinatesAreEqual) {
                    resultRow[i] = 1;
                }
            }

            result.add(resultRow);
        }

        return result;
    }
}
