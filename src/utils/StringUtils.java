package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    /**
     * A function that converts a string to its binary representation.
     * It splits the string into a List of specified size arrays containing binary representation of each character
     * in the string according to the ASCII encoding.
     * @param inputString The string to convert into binary form.
     * @param chunkSize The size of the resulting arrays.
     * @return A list of arrays containing the binary representation of the provided string.
     */
    public static List<int[]> stringToBinaryArrays(String inputString, int chunkSize) {
        StringBuilder binaryStringBuilder = new StringBuilder();

        for (char character : inputString.toCharArray()) {
            binaryStringBuilder.append(String.format("%8s", Integer.toBinaryString(character)).replace(' ', '0'));
        }

        String binaryString = binaryStringBuilder.toString();
        int[] binaryArray = new int[binaryString.length()];
        for (int i = 0; i < binaryString.length(); i++) {
            binaryArray[i] = Character.getNumericValue(binaryString.charAt(i));
        }

        return splitArray(binaryArray, chunkSize);
    }

    /**
     * A helper function that takes a larger array and splits it to smaller arrays of a specified size.
     * If the last array is not of full specified size it will be left as is.
     * @param arrayToSplit The array to split into smaller ones.
     * @param chunkSize The size of the resulting arrays.
     * @return A list of arrays containing the original array but split into smaller ones.
     */
    public static List<int[]> splitArray(int[] arrayToSplit, int chunkSize) {
        List<int[]> arrays = new ArrayList<>();
        int length = arrayToSplit.length;
        int startIndex = 0;

        while (startIndex < length) {
            int endIndex = Math.min(startIndex + chunkSize, length);

            arrays.add(Arrays.copyOfRange(arrayToSplit, startIndex, endIndex));

            startIndex += chunkSize;
        }

        return arrays;
    }

    /**
     * A function that takes a List of binary arrays and returns the corresponding string representation of it according
     * to the ASCII encoding.
     * @param binaryArrays The arrays to get the string representation of.
     * @return The string representation of the provided binary arrays.
     */
    public static String binaryArraysToString(List<int[]> binaryArrays) {
        StringBuilder stringBuilder = new StringBuilder();
        int[] combinedArrays = binaryArrays.stream()
                .flatMapToInt(Arrays::stream)
                .toArray();
        int length = combinedArrays.length;
        int startIndex = 0;

        while (startIndex < length) {
            int decimalValue = 0;
            for (int i = 0; i < 8; i++) {
                decimalValue = (decimalValue << 1) | combinedArrays[startIndex++];
            }

            stringBuilder.append((char) decimalValue);
        }

        return stringBuilder.toString();
    }
}
