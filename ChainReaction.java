import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ChainReaction {

    private static final int MAX_ATTEMPTS = 20;

    private final int guessCount;
    private final int chainLength;
    private final String[] gameWords;
    private final ArrayList<ArrayList<String>> wordSets;
    private final ArrayList<String[]> chainWords = new ArrayList<>();

    private final Random random = new Random();
    private final Scanner input;

    private int currentIndex;

    public ChainReaction(
            int guesses,
            int chainLength,
            ArrayList<ArrayList<String>> wordSets,
            Scanner input){

        this.guessCount = guesses;
        this.chainLength = chainLength;
        this.gameWords = new String[chainLength];
        this.wordSets = wordSets;
        this.input = input;

        currentIndex = 1;
    }

    public void playGame(){

        int guesses = 0;

        if(!generateChain()){
            System.out.println("Unable to generate a valid chain. Please restart.");
            return;
        }
        createChain();
        revealNextLetter();
        revealChainWord(0);
        revealChainWord(chainLength - 1);
        showChain();

        while(guesses < guessCount){
            System.out.println("Guesses Remaining: " + (guessCount - guesses));
            System.out.print("Enter a guess for word " + (currentIndex + 1) + " :");

            String guess = input.nextLine().trim().toLowerCase();

            if(guess.equals(gameWords[currentIndex])){

                System.out.println("\nCorrect!....The word was " + gameWords[currentIndex]);

                revealChainWord(currentIndex);
                currentIndex++;



                if(currentIndex == chainLength - 1){
                    System.out.println("\nCONGRATULATIONS!  YOU HAVE COMPLETED THE CHAIN!\n");
                    break;
                }

                showChain();
            }
            else{
                System.out.println("\nIncorrect....Try Again");

                revealNextLetter();
                showChain();

                boolean wordCompleted = true;

                for(String letter : chainWords.get(currentIndex)){
                    if(letter.equals("_")){
                        wordCompleted = false;
                        break;
                    }
                }

                if(wordCompleted){
                    System.out.println("<<The word has been fully revealed. Enter it to continue>>");
                }
            }

            guesses++;

            if(guesses == guessCount){
                System.out.println("Sorry.  You have run out of guesses :(\n");
                System.out.println("\nGAME OVER! :(");
                System.out.println("Here is the chain:\n");

                showChainWords();
            }
        }
    }
    /**
     * Generates a random chain of the requested length
     * @return true if a valid chain was generated, otherwise false
     */
    private boolean generateChain(){

        if(chainLength < 3 || wordSets.isEmpty()) return false;

        int randomSet = random.nextInt(wordSets.size());
        ArrayList<String> startingSet = wordSets.get(randomSet);

        if(startingSet.size() < 2) return false;

        gameWords[0] = startingSet.get(0);
        String prevWord = startingSet.get(random.nextInt(startingSet.size() - 1) + 1);
        gameWords[1] = prevWord;

        for (int i = 2; i < gameWords.length; i++) {

            boolean wordFound = false;
            int attempts = 0;

            while (!wordFound && attempts < MAX_ATTEMPTS) {

                int matchingSetIndex = findWordSet(prevWord);

                if(matchingSetIndex == -1) return false;

                ArrayList<String> matchingSet = wordSets.get(matchingSetIndex);

                if(matchingSet.size() < 2) return false;

                String candidateWord = matchingSet.get(random.nextInt(matchingSet.size() - 1) + 1);
                boolean isLastWord = i == gameWords.length - 1;

                if(isLastWord || hasWordSet(candidateWord)){
                    gameWords[i] = candidateWord;
                    prevWord = candidateWord;
                    wordFound = true;
                }

                attempts++;
            }
            if(!wordFound) return false;
        }

        return true;
    }

    /**
     * Finds the word set whose first word matches the supplied word
     *
     * @return the matching set index, or -1 if non exists
     */
    private int findWordSet(String word){

        for(int i = 0; i < wordSets.size(); i++){

            ArrayList<String> wordSet = wordSets.get(i);

            if(!wordSet.isEmpty() && word.equals(wordSet.get(0)))
                return i;
        }

        return -1;

    }

    /**
     * Determines whether a word can continue the chain
     */
    private boolean hasWordSet(String word){
        return findWordSet(word) != -1;
    }

    private void showChainWords(){

        for(String word : gameWords){
            System.out.println(word);
        }

    }

    private void createChain(){

        chainWords.clear();
        for (int i = 0; i < gameWords.length; i++) {
            String[] letters = new String[gameWords[i].length()];
            for (int j = 0; j < gameWords[i].length(); j++) {
                letters[j] = "_";
            }
            chainWords.add(letters);
        }
    }

    private void showChain(){
        for(String[] word : chainWords){
            for(String letter : word){
                System.out.print(letter + " ");
            }
            System.out.println();
        }
    }

    /**
     * Reveals one additional letter from the first unrevealed word
     */
    private void revealNextLetter(){
        String[] currentWord = chainWords.get(currentIndex);

        for(int i = 0; i < currentWord.length; i++){
            if(currentWord[i].equals("_")){
                currentWord[i] = String.valueOf(gameWords[currentIndex].charAt(i));
                return;
            }
        }
    }

    /**
     * Reveals an entire word in the displayed chain
     */
    private void revealChainWord(int index){
        String word = gameWords[index];
        String[] letters = new String[word.length()];

        for(int i = 0; i < word.length(); i++){
            letters[i] = String.valueOf(word.charAt(i));
        }

        chainWords.set(index, letters);
    }
}
