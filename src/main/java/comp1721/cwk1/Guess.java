package comp1721.cwk1;

import java.util.Locale;
import java.util.Scanner;

public class Guess {
  // Use this to get player input in readFromPlayer()
  private static final Scanner INPUT = new Scanner(System.in);

  private int guessNumber;
  private String chosenWord;

  public Guess(int num) {
    if (num<1 || num>6) {
      throw new GameException("Invalid number");
    }
    guessNumber = num;
  }

  public Guess (int num, String word) {
    char[] wordArray = word.toCharArray();
    if (num<1 || num>6) {
      throw new GameException("Invalid number");
    }
    if (word.length() != 5) {
      throw new GameException("Invalid number");
    }
    for (char letter: wordArray) {
      if (!Character.isLetter(letter)) {
        throw new GameException("Invalid word");
      }
    }

    chosenWord = word.toUpperCase();
    guessNumber = num;
  }

  public int getGuessNumber() {
    return guessNumber;
  }

  public String getChosenWord() {
    return chosenWord;
  }

  public void readFromPlayer() {
    chosenWord = INPUT.nextLine().toUpperCase();
    //Check to see if the input is a five letter word
    if (chosenWord.length() != 5) {
      System.out.print("Enter a 5 letter word: ");
      readFromPlayer();
    }
    else {
      //Check if all characters are alphabetic
      char[] wordArray = chosenWord.toCharArray();
      for (char letter: wordArray) {
        if (!Character.isLetter(letter)) {
          System.out.print("Enter a word with alphabetic characters: ");
          readFromPlayer();
          break;
        }
      }
    }
  }


  public String compareWith(String target) {
    //Used to store the letters of the output string with ANSI escape codes
    String[] output = new String[5];
    //To keep track of letters that have already been compared
    String usedIndex = "";

    //Fill the output array with all the letters in grey
    for (int i=0; i<5; i++)
      output[i] = "\033[30;107m "+chosenWord.charAt(i)+" \033[0m";

    //Check which letters are correct and in correct place
    for (int i=0; i<5; i++) {
      if (chosenWord.charAt(i) == target.charAt(i)) {
        output[i] = "\033[30;102m "+chosenWord.charAt(i)+" \033[0m";
        usedIndex += Integer.toString(i);
      }
    }

    //Check which letters are correct but in the wrong place
    for (int i=0; i<5; i++) {
      for (int j=0; j<5; j++) {
        if (usedIndex.contains(Integer.toString(j))) {
          continue;
        }
        else if (chosenWord.charAt(i) == target.charAt(j)) {
          output[i] = "\033[30;103m "+chosenWord.charAt(i)+" \033[0m";
          usedIndex += Integer.toString(j);
          break;
        }
      }
    }

    //Build the output sting from the output array
    String outputString = "";
    for (int i=0; i<5; i++)
      outputString += output[i];
    return outputString;
  }


  public boolean matches(String target) {
    return target.equals(chosenWord);
  }
}
