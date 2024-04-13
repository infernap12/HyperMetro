package metro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MetroSystem.class, new MetroSystemJsonDeserialiser())
                .create();
        MetroSystem metroSystem = null;
        try (FileReader fileReader = new FileReader(args[0]);
             BufferedReader br = new BufferedReader(fileReader)) {
            metroSystem = gson.fromJson(br, MetroSystem.class);
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
            System.exit(1);
        }

        while (true) {
            Command command = new Command(ArgumentTokenizer.tokenize(SCANNER.nextLine()));
        }

    }
}

