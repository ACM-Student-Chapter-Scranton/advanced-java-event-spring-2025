import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Blackjack {
    // Associates a rank with a value. Used to calculate the value of a card.
    private static final Map<Rank, Integer> RANK_VALUES = new HashMap<>();

    // Any code put in a static block is executed when the class is initialized.
    static {
        // the values() method returns an array of every enum constant
        // In this case, it returns [ACE, TWO, ..., QUEEN, KING] 
        Rank[] ranks = StandardRank.values();

        /*
         * TODO
         * Numbered cards are worth their number, and face cards are worth 10.
         * Aces are worth either 1 or 11, but for this map, assume they are worth 11.
         * Can you add each rank and value to RANK_VALUES?
         */

    }

    private Deck deck;
    private List<Card> dealerHand;
    private List<Card> playerHand;
    private int chipCount;
    private int bet;
    private Scanner input;
    private String gameName;

    public Blackjack(Scanner input, String gameName) {
        chipCount = 500;
        deck = new StandardDeck();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        this.input = input;
        this.gameName = gameName;
    }

    public void run() {
        returnCardsToDeck();
        deck.shuffle();
        bet = getBet();

        // first round of dealing
        dealCardTo(playerHand, true);
        dealCardTo(dealerHand, true);

        // second round of dealing, dealer's new card face down
        dealCardTo(playerHand, true);
        dealCardTo(dealerHand, false);

        // calculate initial hands
        int playerTotal = totalUpHand(playerHand);
        int dealerTotal = RANK_VALUES.get(dealerHand.get(0).getRank());

        // do player's turn
        boolean playerTurn = totalUpHand(playerHand) < 21;
        while (playerTurn) {
            printHands();
            // '\n' is the newline character, which indicates the end of a line in most Unix-like and modern Mac systems.
            // May work on Windows, though Windows usually ends lines with "\r\n".
            System.out.println("\nHit or stand?");

            String decision = input.nextLine();
            
            // You could use a switch statement here, but it's not as useful when cases are several lines long.
            if (decision.equals("hit")) {
                dealCardTo(playerHand, true);
                playerTotal = totalUpHand(playerHand);
                playerTurn = playerTotal < 21;
            } else if (decision.equals("stand")) {
                playerTurn = false;
            } else {
                System.out.println("Unknown command.");
            }

            System.out.println();
        }

        boolean playerBust = playerTotal > 21;

        // reveal hole card
        dealerHand.get(1).faceUp();
        dealerTotal = totalUpHand(dealerHand);

        // continue dealer's turn
        boolean dealerTurn = dealerTotal < 17;
        while (dealerTurn) {
            dealCardTo(dealerHand, true);
            dealerTotal = totalUpHand(dealerHand);
            dealerTurn = dealerTotal < 17;
        }

        boolean dealerBust = dealerTotal > 21;

        printHands();

        // determine outcome and print results
        if (playerBust) {
            chipCount -= bet;
            System.out.println("You busted!");
            System.out.printf("You lose %d chips!%n", bet);
        } else if (dealerBust) {
            chipCount += bet;
            System.out.println("Dealer busted!");
            System.out.printf("You win %d chips!%n", bet);
        } else {
            if (playerTotal > dealerTotal) {
                chipCount += bet;
                System.out.printf("You win %d chips!%n", bet);
            } else if (playerTotal < dealerTotal) {
                chipCount -= bet;
                System.out.printf("You lose %d chips!%n", bet);
            } else {
                System.out.println("Tie!");
                System.out.println("Your bet is returned.");
            }
        }

        System.out.printf("You have a total of %d chips.", chipCount);

        saveGame();
    }

    /*
     * TODO
     * Can you use List operations and for-each constructs to return
     * the cards from the player's and dealer's hands back to the deck?
     */
    private void returnCardsToDeck() {   
        // TODO
    }

    private void dealCardTo(List<Card> hand, boolean faceUp) {
        Card card = deck.removeTopCard();
        if (faceUp) {
            card.faceUp();
        } else {
            card.faceDown();
        }
        hand.add(card);
    }

    private int getBet() {
        int bet = 0;
        do {
            System.out.print("Enter bet: ");
            try {
                bet = Integer.parseInt(input.nextLine());
                if (bet <= 0) {
                    System.out.println("You must make a bet of at least 1 chip.");
                }
                if (bet > chipCount) {
                    System.out.println("You cannot bet more chips than you have.");
                }
            } catch (NumberFormatException e) {
                System.out.println("You may only enter digit characters");
            }
            
        } while (bet <= 0 || bet > chipCount);

        return bet;
    }

    private int totalUpHand(List<Card> hand) {
        int total = 0;
        int numAces = 0;
        for (Card card : hand) {
            total += RANK_VALUES.get(card.getRank());
            if (card.getRank() == StandardRank.ACE) {
                numAces++;
            }
        }

        while (total > 21 && numAces > 0) {
            total -= 10;
        }

        return total;
    }

    private void printHands() {
        System.out.println("Dealer's hand: " + dealerHand);
        System.out.println("Player's hand: " + playerHand);
    }

    private static Card getCardFromString(String s) {
        String rankSymbol = s.substring(0, s.length() - 1);
        char suitSymbol = s.charAt(s.length() - 1);

        StandardRank rank = null;
        switch (rankSymbol) {
            case "A": rank = StandardRank.ACE;
                      break;
            case "2": rank = StandardRank.TWO;
                      break;
            case "3": rank = StandardRank.THREE;
                      break;
            case "4": rank = StandardRank.FOUR;
                      break;
            case "5": rank = StandardRank.FIVE;
                      break;
            case "6": rank = StandardRank.SIX;
                      break;
            case "7": rank = StandardRank.SEVEN;
                      break;
            case "8": rank = StandardRank.EIGHT;
                      break;
            case "9": rank = StandardRank.NINE;
                      break;
            case "10": rank = StandardRank.TEN;
                       break;
            case "J": rank = StandardRank.JACK;
                       break;
            case "Q": rank = StandardRank.QUEEN;
                      break;
            case "K": rank = StandardRank.KING;
                      break;
            default:  throw new IllegalArgumentException();
        }

        /*
         * TODO
         * Can you write out a switch statement to determine the suit?
         */
        FrenchSuit suit = null;
        

        return new Card(rank, suit);
    }

    /*
     * TODO
     * Can you use the for-each construct as well as the writer.write and writer.newLine methods
     * to write the data?
     * An example is provided in oldgames/example.txt. Here is what each line means:
     * 
     * 1. the bet
     * 2. dealer's hand
     * 3. player's hand
     * 4. total chips
     */
    public void saveGame() {
        // This creates a relative path. System.getProperty("user.dir") gets the path of the working directory, which is usually the folder in which the main method is run.
        // The following arguments combine to the working directory to create a file path.
        Path saveLocation = Paths.get(System.getProperty("user.dir"), "oldgames", String.format("%s.txt", gameName));
        try (BufferedWriter writer = Files.newBufferedWriter(saveLocation)) {
            System.out.println();

            // TODO

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewOldGame(String gameName) {
        // This creates a relative path. System.getProperty("user.dir") gets the path of the working directory, which is usually the folder in which the main method is run.
        // The following arguments combine to the working directory to create a file path.
        Path saveLocation = Paths.get(System.getProperty("user.dir"), "oldgames", String.format("%s.txt", gameName));
        try (BufferedReader reader = Files.newBufferedReader(saveLocation)) {
            int bet = Integer.parseInt(reader.readLine());

            /*
             * TODO
             * Can you use a Scanner and the getCardFromString method to fill the dealer's hand?
             */
            List<Card> dealerHand = new ArrayList<>();
            

            /*
             * TODO
             * Can you use a Scanner and the getCardFromString method to fill the player's hand?
             */
            List<Card> playerHand = new ArrayList<>();
            

            int chipCount = Integer.parseInt(reader.readLine());

            /*
             * TODO
             * Can you change these to printf statements?
             */
            System.out.println("Bet amount: " + bet + "\n");
            System.out.println("Dealer's hand: " + dealerHand + "\n");
            System.out.println("Player's hand: " + playerHand + "\n");
            System.out.println("Total chip amount: " + chipCount + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // It's good practice to wrap a Closeable like Scanner in a try-with-resourcse block because it will automatically close when it reaches the end.
        try (Scanner input = new Scanner(System.in)) {
            // The ?: operator is shorthand for if-then-else. Use it sparingly for readability purposes.
            // if no arguments are passed into the program, save the game to the default file name, or use the first argument as the file name
            String gameName = args.length == 0 ? "default" : args[0];
            System.out.println(System.getProperty("user.dir"));
            System.out.print("New game or view old game (new/view)? ");
            
            switch (input.nextLine()) {
                case "new": Blackjack game = new Blackjack(input, gameName);
                            game.run();
                            break;
                case "view": Blackjack.viewOldGame(gameName);
                             break;
                default: System.out.println("Invalid option");
            }
        }
    }
}
