import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/*Jin-gyu Shim
* Chain Reaction is a console-based word chain game.
* The program loads a word dataset, cleans the data,
* and allows the player to select a difficulty or create
* a custom game.
*/

public class ChainReactionMain {

    private static final String WORD_LIST_FILE = "wordList.txt";

    private static final int EXPECTED_WORD_COUNT = 8033;
    private static final int EXPECTED_WORD_SET_COUNT = 2334;

    private static final int MIN_CHAIN_LENGTH = 3;
    private static final int MIN_GUESSES = 1;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        ArrayList<ArrayList<String>> wordSets = loadWordSets();

        if (wordSets == null) {
            input.close();
            return;
        }

        cleanData(wordSets);

        boolean playing = true;

        while (playing) {

            printMenu();

            int difficulty = readInt(input, "SELECT DIFFICULTY: ");

            int[] gameSettings = getGameSettings(difficulty, input);

            if (gameSettings == null) {
                System.out.println("Invalid Selection...\n");
                continue;
            }

            int chainLength = gameSettings[0];
            int guesses = gameSettings[1];

            ChainReaction game = new ChainReaction(
                    guesses,
                    chainLength,
                    wordSets,
                    input);

            game.playGame();

            playing = askToPlayAgain(input);
        }

        System.out.println("\nTHANK YOU FOR PLAYING!!");

        input.close();
    }

    /**
     * Loads the word dataset from the word list file.
     *
     * @return the loaded word sets, or null if the file could not be found
     */
    private static ArrayList<ArrayList<String>> loadWordSets() {

        ArrayList<ArrayList<String>> wordSets = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(WORD_LIST_FILE))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) continue;

                String[] words = line.split(",");

                ArrayList<String> wordSet = new ArrayList<>();

                for (String word : words) {
                    wordSet.add(word.trim());
                }

                wordSets.add(wordSet);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Error: could not find file: %s%n", WORD_LIST_FILE);
            return null;
        }

        return wordSets;
    }

    /**
     * Displays the game difficulty menu.
     */
    private static void printMenu() {
        System.out.println("*********************************");
        System.out.println("*                               *");
        System.out.println("*       CHAIN REACTION          *");
        System.out.println("*  CAN YOU COMPLETE THE CHAIN?  *");
        System.out.println("*                               *");
        System.out.println("*********************************\n");

        System.out.println("Tutorial..................press 0");
        System.out.println("Beginner..................press 1");
        System.out.println("Pro.......................press 2");
        System.out.println("Superstar.................press 3");
        System.out.println("Custom....................press 4\n");
    }

    /**
     * Returns the chain length and guess count for the selected difficulty.
     *
     * @return an array containing {chainLength, guesses},
     * or null if the selection is invalid
     */
    private static int[] getGameSettings(int difficulty, Scanner input) {

        switch (difficulty) {
            case 0:
                return new int[]{3, 3};
            case 1:
                return new int[]{5, 8};
            case 2:
                return new int[]{7, 12};
            case 3:
                return new int[]{9, 16};
            case 4:
                int chainLength = readInt(input, "Enter Chain Length: ");

                while (chainLength < MIN_CHAIN_LENGTH) {
                    System.out.printf("Chain length must be at least %d.%n", MIN_CHAIN_LENGTH);

                    chainLength = readInt(input, "Enter Chain Length: ");
                }

                int guesses = readInt(input, "Enter Number of Guesses: ");

                while (guesses < MIN_GUESSES) {
                    System.out.printf("Number of guesses must be at least %d.%n", MIN_GUESSES);

                    guesses = readInt(input, "Enter Number of Guesses: ");
                }

                return new int[]{chainLength, guesses};
            default:
                return null;
        }
    }

    /**
     * Reads an integer from the user and continues prompting
     * until a valid integer is entered.
     */
    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);

            String value = input.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number");
            }
        }
    }

    /**
     * Asks whether the player wants to start another game
     */
    private static boolean askToPlayAgain(Scanner input) {
        while (true) {
            System.out.println("\nPlay Again (y)es or (n)o");
            String option = input.nextLine().trim().toLowerCase();

            if (option.equals("y")) return true;
            if (option.equals("n")) return false;

            System.out.println("Invalid selection... Please enter y or n");
        }
    }

    /**
     * Cleans the word dataset by repeatedly removing words
     * that cannot participate in a valid chain and removing
     * word sets that contain fewer than two words
     */
    private static void cleanData(ArrayList<ArrayList<String>> wordSets) {

        boolean changed;

        do{
            changed = false;

            Set<String> firstWords = new HashSet<>();

            for(ArrayList<String> wordSet: wordSets){
                if(!wordSet.isEmpty()){
                    firstWords.add(wordSet.get(0));
                }
            }

            for(int i = 0; i < wordSets.size(); i++){
                ArrayList<String> currentSet = wordSets.get(i);
                ArrayList<String> cleanedSet = new ArrayList<>();

                for(String word: currentSet){
                    if(firstWords.contains(word)){
                        cleanedSet.add(word);
                    }
                }

                if(!cleanedSet.equals(currentSet)){
                    changed = true;
                }

                wordSets.set(i, cleanedSet);
            }

            Iterator<ArrayList<String>> iterator = wordSets.iterator();

            while(iterator.hasNext()){
                ArrayList<String> wordSet = iterator.next();

                if(wordSet.size() < 2){
                    iterator.remove();
                    changed = true;
                }
            }
        } while(changed);

        validate(wordSets);
    }

    /**
     * Validates the cleaned dataset against the expected values;
     */
    private static void validate(ArrayList<ArrayList<String>> wordSets) {

        int totalWordCount = 0;

        for (ArrayList<String> wordSet : wordSets) {
            totalWordCount += wordSet.size();
        }

        System.out.println("Total Word Count: " + totalWordCount);
        System.out.println("Number of Word Sets: " + wordSets.size());

        boolean datasetIsComplete = totalWordCount == EXPECTED_WORD_COUNT && wordSets.size() == EXPECTED_WORD_SET_COUNT;


        String status = datasetIsComplete? "Complete" : "Incomplete";

        System.out.println("Dataset Cleaning: " + status);
    }
}