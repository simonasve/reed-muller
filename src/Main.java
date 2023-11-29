import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static utils.ImageUtils.*;
import static utils.StringUtils.*;

public class Main {
    private static ReedMuller reedMuller;
    private static Channel channel;
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int m;
        int r;
        double probabilityOfError;

        System.out.println("Please enter the value m >= 0 (integer):");
        while (true) {
            String mString = scanner.nextLine();

            try {
                m = Integer.parseInt(mString);

                if (m < 0) {
                    throw new IllegalArgumentException();
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer value. Please, try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a value that is >= 0:");
            }
        }

        System.out.println("Please enter the value 0 >= r <= m (integer):");
        while (true) {
            String rString = scanner.nextLine();

            try {
                r = Integer.parseInt(rString);

                if (r > m || r < 0) {
                    throw new IllegalArgumentException();
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer value. Please, try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a value that is 0 >= r <= m:");
            }
        }

        System.out.println("Please enter the probability of error that is >= 0 but <= 1 (floating):");
        while (true) {
            String errorString = scanner.nextLine();

            try {
                probabilityOfError = Double.parseDouble(errorString);

                if (probabilityOfError < 0 || probabilityOfError > 1) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid floating point value. Please, try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a value that is >= 0 but <= 1:");
            }
        }

        reedMuller = new ReedMuller(m, r);
        channel = new Channel(probabilityOfError);

        System.out.println("Please select an option:");
        while (true) {
            System.out.println("1. One word input");
            System.out.println("2. Sentence input");
            System.out.println("3. Image input");
            String choice = scanner.nextLine();

            if (Objects.equals(choice, "1")) {
                handleFirstOption();
                break;
            } else if (Objects.equals(choice, "2")) {
                handleSecondOption();
                break;
            } else if (Objects.equals(choice, "3")) {
                handleThirdOption();
                break;
            } else {
                System.out.println("Option not available, please try again:");
            }
        }
    }

    /**
     * A function that handles the first case of the program. That is where the user himself inputs a sequence to be
     * processed.
     */
    private static void handleFirstOption() {
        int[] word = new int[reedMuller.wordLength()];

        System.out.println("Please enter a word of length " + reedMuller.wordLength()
                + " that consists of 0 or 1 and each number is separated by a comma (,):");
        while (true) {
            String wordString = scanner.nextLine();

            try {
                String[] wordStrings = wordString.split(",");

                if (wordStrings.length != reedMuller.wordLength()) {
                    throw new IllegalArgumentException();
                }

                for (int i = 0; i < wordStrings.length; i++) {
                    if (!Objects.equals(wordStrings[i], "0") && !Objects.equals(wordStrings[i], "1")) {
                        throw new IllegalArgumentException();
                    }

                    word[i] = Integer.parseInt(wordStrings[i]);
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer value in word. Please, try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid word length or illegal character detected. Please, try again:");
            }
        }

        int[] encodedWord = reedMuller.encode(word);
        System.out.println("The encoded word is " + Arrays.toString(encodedWord));

        int[] wordSentThroughChannel = channel.sendTroughChannel(encodedWord);
        System.out.println("The word that came out of the channel is " + Arrays.toString(wordSentThroughChannel));

        int amountOfMistakes = 0;
        StringBuilder positionsOfMistakes = new StringBuilder();
        for (int i = 0; i < encodedWord.length; i++) {
            if (encodedWord[i] != wordSentThroughChannel[i]) {
                amountOfMistakes++;

                if (amountOfMistakes == 1) {
                    positionsOfMistakes.append(i + 1);
                } else {
                    positionsOfMistakes.append(" ").append(i + 1);
                }
            }
        }

        if (amountOfMistakes == 0) {
            System.out.println("There were no mistakes made when sending the word through the channel");
        } else {
            System.out.println("There were " + amountOfMistakes + " mistakes made in positions " + positionsOfMistakes);
        }

        System.out.println("If you want to edit the word that is going to be sent for decoding enter a new value of length "
                + reedMuller.blockLength() + " that consists of 0 or 1 and each number is separated by a comma (,), otherwise press enter");
        while (true) {
            String wordString = scanner.nextLine();

            if (Objects.equals(wordString, "")) {
                break;
            }

            try {
                String[] wordStrings = wordString.split(",");
                int[] tempWord = new int[reedMuller.blockLength()];

                if (wordStrings.length != reedMuller.blockLength()) {
                    throw new IllegalArgumentException();
                }

                for (int i = 0; i < wordStrings.length; i++) {
                    if (!Objects.equals(wordStrings[i], "0") && !Objects.equals(wordStrings[i], "1")) {
                        throw new IllegalArgumentException();
                    }

                    tempWord[i] = Integer.parseInt(wordStrings[i]);
                }

                wordSentThroughChannel = tempWord;
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer value in word. Please, try again: ");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid word length ir illegal character detected. Please, try again: ");
            }
        }

        int[] decodedWord = reedMuller.decode(wordSentThroughChannel);

        System.out.println("The decoded word is " + Arrays.toString(decodedWord));
    }

    /**
     * A function that handles the second case of the program. That is where the user inputs a sentence to be processed.
     */
    private static void handleSecondOption() {
        System.out.println("Please enter a sentence to send through the channel:");
        String sentence = scanner.nextLine();
        List<int[]> binaryArrays = stringToBinaryArrays(sentence, reedMuller.wordLength());
        List<int[]> resultWithoutCode = new ArrayList<>();
        List<int[]> resultWithCode = new ArrayList<>();
        int paddingAmount = 0;

        for (int[] binaryArray: binaryArrays) {
            int[] word = binaryArray;
            resultWithoutCode.add(channel.sendTroughChannel(word));

            if (word.length < reedMuller.wordLength()) {
                word = Arrays.copyOfRange(word, 0, reedMuller.wordLength());
                paddingAmount = reedMuller.wordLength() - binaryArray.length;
            }

            int[] encodedWord = reedMuller.encode(word);
            int[] wordSentThroughChannel = channel.sendTroughChannel(encodedWord);
            int[] decodedWord = reedMuller.decode(wordSentThroughChannel);

            if (paddingAmount > 0) {
                decodedWord = Arrays.copyOfRange(decodedWord, 0, reedMuller.wordLength() - paddingAmount);
            }

            resultWithCode.add(decodedWord);
        }

        String withoutCode = binaryArraysToString(resultWithoutCode);
        String withCode = binaryArraysToString(resultWithCode);


        System.out.println("Resulting sentence without code: " + withoutCode);
        System.out.println("Resulting sentence with code: " + withCode);
    }

    /**
     * A function that handles the third case of the program. That is where the user inputs an image to be processed.
     */
    private static void handleThirdOption() {
        List<int[]> binaryArrays;
        BufferedImage originalImage;
        List<int[]> resultWithoutCode = new ArrayList<>();
        List<int[]> resultWithCode = new ArrayList<>();
        int paddingAmount = 0;

        System.out.println("Please enter the filename of the image you want processed:");
        while (true) {
            System.out.println("Available images: 1 2 3");
            String imageFilename = scanner.nextLine();
            String imageFilepath = System.getProperty("user.dir") + "\\src\\images\\" + imageFilename + ".bmp";

            try {
                originalImage = ImageIO.read(new File(imageFilepath));
                if (originalImage != null) {
                    binaryArrays = imageToBinaryArrays(originalImage, reedMuller.wordLength());
                    displayImage(originalImage, "Original Image");
                    break;
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                System.out.println("Failed to read the image. Please, try again:");
            }
        }

        for (int[] binaryArray: binaryArrays) {
            int[] word = binaryArray;
            resultWithoutCode.add(channel.sendTroughChannel(word));

            if (word.length < reedMuller.wordLength()) {
                word = Arrays.copyOfRange(word, 0, reedMuller.wordLength());
                paddingAmount = reedMuller.wordLength() - binaryArray.length;
            }

            int[] encodedWord = reedMuller.encode(word);
            int[] wordSentThroughChannel = channel.sendTroughChannel(encodedWord);
            int[] decodedWord = reedMuller.decode(wordSentThroughChannel);

            if (paddingAmount > 0) {
                decodedWord = Arrays.copyOfRange(decodedWord, 0, reedMuller.wordLength() - paddingAmount);
            }

            resultWithCode.add(decodedWord);
        }

        BufferedImage imageWithoutCode = binaryArraysToImage(resultWithoutCode, originalImage.getWidth(), originalImage.getHeight());
        BufferedImage imageWithCode = binaryArraysToImage(resultWithCode, originalImage.getWidth(), originalImage.getHeight());

        displayImage(imageWithoutCode, "Without Code");
        displayImage(imageWithCode, "With Code");
    }
}