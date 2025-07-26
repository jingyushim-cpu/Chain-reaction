import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**Enter your name here**/

public class ChainReactionMain {

    public static void main(String[] args){

        ArrayList<ArrayList<String>> wordSets = new ArrayList<>();
        String filename = "wordList.txt";

        try{
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String words = scanner.nextLine();
                String[] wordArray = words.split(",");
                ArrayList<String> row = new ArrayList<>(Arrays.asList(wordArray));
                wordSets.add(row);
            }
        }
        catch (FileNotFoundException e){
            System.out.printf("File %s does not exist\n",filename);
        }

        cleanData(wordSets);

        while(true)
        {
            int chainLength = 0;
            int guesses = 0;

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
            System.out.print("SELECT DIFFICULTY: ");
            int difficulty = new Scanner(System.in).nextInt();
            switch (difficulty) {
                case 0:{
                    chainLength = 3;
                    guesses = 3;
                    break;
                }
                case 1:{
                    chainLength = 3;
                    guesses = 10;
                    break;
                }
                case 2: {
                    chainLength = 5;
                    guesses = 15;
                    break;
                }
                case 3: {
                    chainLength = 7;
                    guesses = 20;
                    break;
                }
                default: {
                    System.out.print("Enter Chain Length: ");
                    chainLength = new Scanner(System.in).nextInt();
                    System.out.print("Enter Number of Guesses: ");
                    guesses = new Scanner(System.in).nextInt();
                    break;
                }
            }

            ChainReaction c = new ChainReaction(guesses,chainLength, wordSets);
            c.playGame();
            System.out.println("\nPlay Again (y)es or (n)o");
            char option = new Scanner(System.in).next().toLowerCase().charAt(0);
            if(option == 'n'){
                System.out.println("\nTHANK YOU FOR PLAYING!");
                break;
            }
        }
    }

    public static void cleanData(ArrayList<ArrayList<String>> wordSets){

        int previous = 0; // later used in while loop

        //while loop until all the fixes are done
        while(previous != wordSets.size()) {
            previous = wordSets.size(); // get out of the while loop when there is no more fix to be done


            ArrayList<String> firstWords = new ArrayList<>();       //arrayList for first words from each row
            for (ArrayList<String> row : wordSets) {
                firstWords.add(row.get(0));
            }

            //for loop to go through each row
            for (int i = 0; i < wordSets.size(); i++) {
                ArrayList<String> row = wordSets.get(i);
                ArrayList<String> newRow = new ArrayList<>(); // new row to remove words that never appear first in any row

                //add word to newRow if the word appears first in some row
                for (String word : row) {
                    if (firstWords.contains(word)) {
                        newRow.add(word);
                    }
                }
                //update wordSets with newRow
                wordSets.set(i, newRow);
            }

            //remove the row if it only contains one word
            for (int i = 0; i < wordSets.size(); i++) {
                if (wordSets.get(i).size() < 2) {
                    wordSets.remove(i);
                    i--; //because row has been removed
                }
            }
        }
	    
        validate(wordSets);
    }
    public static void validate(ArrayList<ArrayList<String>> wordSets){
        final int wordCountValid = 8033;
        final int wordSetCountValid = 2334;

        int numTotalWords = 0;
        for (ArrayList<String> wordSet : wordSets) {
            for (int j = 0; j < wordSet.size(); j++) {
                numTotalWords++;
            }
        }
        System.out.println("Total Word Count: " + numTotalWords);
        System.out.println("Number of Unique Words: " + wordSets.size());
        String status = "Incomplete";
        if(wordCountValid == numTotalWords && wordSetCountValid == wordSets.size()){
            status = "Complete";
        }
        System.out.println("Dataset Cleaning: " + status);
    }
}
