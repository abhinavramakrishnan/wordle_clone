package comp1721.cwk1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.nio.file.Paths;

public class WordList {

    //Creates an ArrayList to store the words
    private List<String> words = new ArrayList<>();

    public WordList(String filename) throws IOException {
        Scanner input = new Scanner(Paths.get(filename));
        //Reads in words from the file to the ArrayList
        while (input.hasNextLine()) {
            words.add(input.nextLine());
        }
        input.close();
    }

    public int size() {
       return words.size();
    }

    public String getWord(int n) {
        //Check if gameNumber/n is within allowed range
        if (n<0 || n>=words.size()) {
            throw new GameException("Invalid number");
        }
        return words.get(n);
    }
}
