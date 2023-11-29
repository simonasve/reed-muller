package factories;

import java.util.ArrayList;
import java.util.List;

public class CombinationFactory {
    private final int m;
    private final List<Integer> interval = new ArrayList<>();

    public CombinationFactory(int m) {
        this.m = m;
        for (int i = 0; i < m; i++) {
            interval.add(i);
        }
    }

    /**
     * A function that creates all possible row combinations for a given order
     * @param order Which order of combinations to return
     * @return The row combinations for that order
     */
    public List<List<Integer>> createRowCombinations(int order) {
        List<List<Integer>> result = new ArrayList<>();

        combine(order, 0, new ArrayList<>(), result);

        return result;
    }

    /**
     * A recursive helper function that fills the provided collection with appropriate row combinations
     * @param order Initial order
     * @param start The starting index used to denote the current row index
     * @param current A collection used to store the current combination indexes
     * @param result A collection used to store the resulting row combinations
     */
    private void combine(int order, int start, List<Integer> current, List<List<Integer>> result) {
        if (order == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < interval.size(); i++) {
            current.add(interval.get(i));
            combine(order - 1, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * A function that takes a combination and creates a complement of that combination
     * @param combination The combination whose complement should be returned
     * @return Complement of the provided combination
     */
    public List<Integer> createComplementCombination(List<Integer> combination) {
        List<Integer> complementCombination = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            if (!combination.contains(i)) {
                complementCombination.add(i);
            }
        }

        return complementCombination;
    }
}
