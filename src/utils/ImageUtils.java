package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static utils.StringUtils.splitArray;

public class ImageUtils {

    /**
     * A function that converts an image to its binary representation.
     * It splits the image into a List of specified size arrays containing binary representation of each of the images
     * pixels RGB value.
     * @param image The image to convert into binary form.
     * @param chunkSize The size of the resulting arrays.
     * @return A list of arrays containing the binary representation of the provided image.
     */
    public static List<int[]> imageToBinaryArrays(BufferedImage image, int chunkSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder binaryStringBuilder = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Get each color as an integer
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Combine into a 24 bit string
                binaryStringBuilder.append(String.format("%8s%8s%8s",
                        Integer.toBinaryString(red),
                        Integer.toBinaryString(green),
                        Integer.toBinaryString(blue))
                        .replace(' ', '0'));
            }
        }

        String binaryString = binaryStringBuilder.toString();
        int[] binaryArray = new int[binaryString.length()];
        for (int i = 0; i < binaryString.length(); i++) {
            binaryArray[i] = Character.getNumericValue(binaryString.charAt(i));
        }

        return splitArray(binaryArray, chunkSize);
    }

    /**
     * A function that takes a List of binary arrays and returns the corresponding image representation of it according
     * to each of the images pixels RGB values.
     * @param binaryArrays The arrays to get the image representation of.
     * @param width The width of the resulting image.
     * @param height The height of the resulting image.
     * @return The image representation of the provided binary arrays.
     */
    public static BufferedImage binaryArraysToImage(List<int[]> binaryArrays, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] combinedArrays = binaryArrays.stream()
                .flatMapToInt(Arrays::stream)
                .toArray();
        int startIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = 0;
                int green = 0;
                int blue = 0;

                //Shift 8 bits for each color
                for (int j = 0; j < 8; j++) {
                    red = (red << 1) | combinedArrays[startIndex++];
                }

                for (int j = 0; j < 8; j++) {
                    green = (green << 1) | combinedArrays[startIndex++];
                }

                for (int j = 0; j < 8; j++) {
                    blue = (blue << 1) | combinedArrays[startIndex++];
                }

                int rgb = (red << 16) | (green << 8) | blue;
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        return bufferedImage;
    }

    /**
     * A function that displays a provided image
     * @param image The image to display
     * @param title The title to give to the image
     */
    public static void displayImage(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(new ImageIcon(image));

        frame.getContentPane().add(label);
        frame.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
