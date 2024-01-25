package comp1721.cwk1;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class Game {
    private int gameNumber;
    private String target;
    private String[] guessHistory = new String[6];

    private boolean correctGuess;
    private int numberOfGuesses;
    private int gamesPlayed;
    private int numberOfWins;
    private int winningStreak;
    private int longestStreak;

    private int[] guessDistribution = new int[6];



    public Game(String filename) throws IOException {
        //Date of the first wordle game
        LocalDate firstWordleGame =  LocalDate.of(2021, 6, 19);
        LocalDate todayDate =  LocalDate.now();
        //Use the two dates to calculate today's game
        long duration = ChronoUnit.DAYS.between(firstWordleGame, todayDate);
        gameNumber = (int) duration;
        WordList todayGame = new WordList(filename);
        target = todayGame.getWord(gameNumber);
    }
    public Game(int num, String filename) throws IOException{
        WordList todayGame = new WordList(filename);
        target = todayGame.getWord(num);
    }



    public void play() {
        System.out.printf("Wordle %d \n\n", gameNumber);

        for (int i=0; i<7; i++) {
            //Player has guessed less than 6 words
            if (i < 6) {
                Guess guess = new Guess(i+1, target);
                System.out.printf("Enter guess (%d/6): ", i+1);
                guess.readFromPlayer();
                System.out.println(guess.compareWith(target));
                //Add the output to history
                guessHistory[i] = guess.compareWith(target);
                if (guess.matches(target)) {
                    if (guess.getGuessNumber() == 1) {
                        System.out.println("Superb - Got it in one!");
                        correctGuess = true;
                        numberOfGuesses = i+1;
                        numberOfWins += 1;
                        gamesPlayed += 1;
                        guessDistribution[i] += 1;
                        break;
                    }
                    else if (guess.getGuessNumber()>1 && guess.getGuessNumber()<6) {
                        System.out.println("Well done!");
                        correctGuess = true;
                        numberOfGuesses = i+1;
                        numberOfWins += 1;
                        gamesPlayed += 1;
                        guessDistribution[i] += 1;
                        break;
                    }
                    else {
                        System.out.println("That was a close call!");
                        correctGuess = true;
                        numberOfGuesses = i+1;
                        numberOfWins += 1;
                        gamesPlayed += 1;
                        guessDistribution[i] += 1;
                        break;
                    }
                }
            }
            //Player has used up all their guess
            else {
                numberOfGuesses = i;
                correctGuess = false;
                gamesPlayed += 1;
                System.out.println("Nope - Better luck next time!");
                System.out.println(target);
                break;
            }

        }
    }


    public void history(String filename) throws IOException{
        Path path = Paths.get(filename);
        //Check if the history file exists otherwise create one
        if (Files.notExists(path)) {
            try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
                out.println("");
            }
        }
        //Read in values from the history file to variables
        Scanner input = new Scanner(Paths.get(filename));
        while(input.hasNextInt()) {
            //Read the first 3 values into a buffer
            int a = input.nextInt();
            boolean b = input.nextBoolean();
            int c = input.nextInt();
            //Read the next 4 values into temporary variables
            gamesPlayed += input.nextInt();
            winningStreak = input.nextInt();
            longestStreak = input.nextInt();
            numberOfWins += input.nextInt();

            //Store each value in the array to the file
            for (int i=0; i<6; i++) {
                guessDistribution[i] += input.nextInt();
            }
        }
        input.close();

        //Update the value in the history files with new values
        try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
            //Update game number on line 1
            out.println(gameNumber);
            //Did player guess the word
            out.println(correctGuess);
            //Update number of guesses
            out.println(numberOfGuesses);
            //Update games played
            out.println(gamesPlayed);

            //Update win streak
            if (correctGuess) {
                winningStreak += 1;
                out.println(winningStreak);
            }
            if (!correctGuess)  {
                winningStreak = 0;
                out.println(winningStreak);
            }
            if (winningStreak >= longestStreak) {
                longestStreak = winningStreak;
                out.println(longestStreak);
            }
            if (winningStreak < longestStreak) {
                out.println(longestStreak);
            }
            out.println(numberOfWins);

            //Update values in the array
            for (int i=0; i<6; i++) {
                out.println(guessDistribution[i]);
            }
        }
    }

    public void statistics() {
        System.out.print("\n----------------------------------------------------------------\n");
        System.out.println("Statistics\n");
        System.out.printf("Played: %d\n", gamesPlayed);
        System.out.printf("Win %%: %.0f%%\n", ((float)numberOfWins/(float)gamesPlayed)*100);
        System.out.printf("Current Streak: %d\n", winningStreak);
        System.out.printf("Max Streak: %d\n", longestStreak);

        //Display a histogram of the guess distribution array
        System.out.println("\nGuess distribution");
        for (int i=0; i<6; i++) {
            System.out.printf("%d: ",i+1);
            for (int j=0; j<guessDistribution[i]; j++) {
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }

    public void save(String filename) throws IOException{
        Path path = Paths.get(filename);
        try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
            //Takes each element(word) in history and adds it to the file
            for (String word: guessHistory) {
                if (word != null) {
                    out.println(word);
                }
            }
        }
    }
}
