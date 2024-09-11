import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a map to store key handlers
        Map<String, KeyHandler> handlers = new HashMap<>();
        handlers.put("-t", new TitleHandler());
        handlers.put("-b{", new BoldHandler());
        handlers.put("-it{", new ItalicHandler());
        handlers.put("-cl{", new CodeHandler());

        System.out.println("Give a path:");
        String input = scanner.nextLine();
        File file = new File(input);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) { // Read entire lines
                    int colonIndex = line.indexOf(":");
                    String l = (colonIndex != -1) ? line.substring(colonIndex + 1).trim() : line.trim(); // Safely extract text after ":"
                    boolean handled = false;
                    for (Map.Entry<String, KeyHandler> entry : handlers.entrySet()) {
                        String key = entry.getKey();
                        KeyHandler handler = entry.getValue();
                        if (l.contains(key)) {
                            handled = true;
                            handler.handle(l);
                            break;
                        }
                    }
                    if (!handled) {
                        System.out.println(l); // Print the line if no handler is found
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error reading the file");
            }
        } else {
            System.out.println("File not found");
        }

        scanner.close();  // Close the scanner to avoid resource leaks
    }

    // Define a common interface for key handlers
    public interface KeyHandler {
        void handle(String phrase);
    }

    // Implement a key handler for the title
    public static class TitleHandler implements KeyHandler {
        @Override
        public void handle(String phrase) {
            int index = phrase.indexOf("-t");
            if (index != -1 && index + 2 < phrase.length()) {
                char n = phrase.charAt(index + 2);
                if (Character.isDigit(n)) {
                    int nbHashtag = Character.getNumericValue(n);
                    String newPhrase = phrase.substring(index + 3).trim(); // Extracts the phrase after "-tN"
                    System.out.println("#".repeat(nbHashtag) + " " + newPhrase); // Print only the formatted title
                } else {
                    System.out.println("Not a number"); // If the character after "-t" is not a number
                }
            } else {
                System.out.println("Invalid format"); // If the format is incorrect
            }
        }
    }

    public static class BoldHandler implements KeyHandler {
        @Override
        public void handle(String phrase) {
            int index = phrase.indexOf("-b{");
            int closeIndex = phrase.indexOf("}");
            String bsyntax = "**";
            if (index != -1 && closeIndex != -1 && closeIndex > index) {
                String newPhrase = phrase.substring(index + 3, closeIndex).trim();
                System.out.println(bsyntax + newPhrase + bsyntax);
            } else {
                System.out.println("Invalid bold format");
            }
        }
    }

    public static class ItalicHandler implements KeyHandler {
        @Override
        public void handle(String phrase) {
            int index = phrase.indexOf("-it{");
            int closeIndex = phrase.indexOf("}");
            String isyntax = "_";
            if (index != -1 && closeIndex != -1 && closeIndex > index) {
                String newPhrase = phrase.substring(index + 4, closeIndex).trim();
                System.out.println(isyntax + newPhrase + isyntax);
            } else {
                System.out.println("Invalid italic format");
            }
        }
    }

    public static class CodeHandler implements KeyHandler {
        @Override
        public void handle(String phrase) {
            int index = phrase.indexOf("-cl{");
            int closeIndex = phrase.indexOf("}");
            String csyntax = "```";
            if (index != -1 && closeIndex != -1 && closeIndex > index) {
                String newPhrase = phrase.substring(index + 4, closeIndex).trim();
                System.out.println(csyntax + "\n" + newPhrase + "\n" + csyntax);
            } else {
                System.out.println("Invalid code format");
            }
        }
    }
}
