# Chain Reaction

A console-based word chain game written in Java.

The game builds a chain of related words from a word-list dataset. The first and last words are revealed, while the player works through the words in between by entering guesses. Incorrect guesses reveal additional letters as hints, while correct guesses reveal the full word and advance the chain.

> Originally developed as a college coursework project and later revisited and refactored for my personal GitHub portfolio.

## Features

- Randomly generated word chains
- Multiple difficulty levels
- Custom chain length and guess count
- Progressive letter hints after incorrect guesses
- Input validation for menu and custom-game settings
- Dataset cleaning before gameplay
- Replay option after each game
- Console-based interface with no external dependencies

## How the Game Works

A valid chain is generated from the provided word dataset.

For example:

```
B L E E D
_ _ _
_ _ _ _
_ _ _ _
T A P E
```

The player starts by guessing the hidden words in order.

- A **correct guess** reveals the entire word and moves to the next word.
- An **incorrect guess** reveals one additional letter and keeps the player on the current word.
- Each submitted guess counts toward the game's guess limit.
- If all allowed guesses are used, the game ends and the complete chain is displayed.

## Difficulty Levels

| Difficulty | Chain Length | Guesses |
|------------|-------------:|--------:|
| Tutorial | 3 | 3 |
| Beginner | 5 | 8 |
| Pro | 7 | 12 |
| Superstar | 9 | 16 |
| Custom | User defined | User defined |

Custom games require a minimum chain length of 3 and at least 1 guess.

## Project Structure

```
Chain-reaction/
├── ChainReaction.java
├── ChainReactionMain.java
├── wordList.txt
└── README.md
```

### Main Components

**ChainReaction.java**
- Generates a valid random chain
- Manages gameplay and guesses
- Tracks the player's current word
- Reveals hints and completed words
- Displays the current chain

**ChainReactionMain.java**
- Loads the word dataset
- Cleans and validates the dataset
- Displays the main menu
- Handles difficulty selection and user input
- Starts new games and handles replay

**wordList.txt**
- Source dataset used to generate word chains

## Running the Program

This project is a standard Java console application and does not require a build tool or external libraries.

### IntelliJ IDEA

1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Make sure a Java JDK is configured.
4. Run `ChainReactionMain.java`.
5. Keep `wordList.txt` available in the program's working directory so the game can load the dataset.

### Command Line

From the project directory:

```bash
javac ChainReaction.java ChainReactionMain.java
java ChainReactionMain
```

## Technical Concepts

This project demonstrates practical Java fundamentals, including:

- Classes and object-oriented design
- Encapsulation and access modifiers
- Arrays and `ArrayList`
- `HashSet` and `Iterator`
- `Scanner` for console input
- Random number generation
- File I/O
- Exception handling
- Input validation
- Javadoc comments
- Git and GitHub version control

## Data Cleaning

Before gameplay begins, the program cleans the word dataset so that the remaining word sets can participate in a valid chain.

The cleaning process repeatedly removes unusable words and word sets until the dataset stabilizes. The program then reports the resulting word and word-set counts and compares them with the expected dataset values.

## Refactoring

The current version was revisited after the original coursework was completed. The refactoring focused on making the code easier to read, maintain, and reason about.

Examples include:

- Reusing a single `Scanner` for user input
- Reusing a single `Random` instance
- Replacing mutable configuration fields with constants and final fields where appropriate
- Separating menu, input, file-loading, validation, and gameplay responsibilities
- Simplifying chain-generation logic
- Using try-with-resources for file handling
- Improving input validation and error handling
- Removing unnecessary getters, setters, and state variables

## What I Learned

This project reinforced core Java programming concepts while giving me practical experience working with an existing codebase rather than only writing code from scratch.

The refactoring process was especially useful for learning how to improve code structure without changing the intended gameplay behavior.

## Future Improvements

Possible future improvements include:

- Adding automated unit tests
- Separating game logic from console UI more completely
- Improving the chain-generation algorithm
- Adding a graphical or web-based interface
- Tracking player statistics and high scores
