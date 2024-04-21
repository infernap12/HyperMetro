package metro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;

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
            Command command;
            try {
                command = new Command(ArgumentTokenizer.tokenize(SCANNER.nextLine()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command");
                continue;
            }
            if (command.type == Command.CommandType.EXIT) {
                System.out.println("Bye!");
                System.exit(0);
            } else {
                metroSystem.invoke(command);
            }
        }

    }
}

