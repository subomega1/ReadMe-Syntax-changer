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

        System.out.println("Enter your input:");
        String input = scanner.nextLine();

        // Process the input
        boolean handled = false;
        for (Map.Entry<String, KeyHandler> entry : handlers.entrySet()) {
            String key = entry.getKey();
            KeyHandler handler = entry.getValue();
            if (input.contains(key)) {
                handled = true;
                handler.handle(input);
                break;
            }
        }

        if (!handled) {
            System.out.println(input); // Print the input if no handler is found
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
