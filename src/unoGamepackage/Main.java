package unoGamepackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();

        System.out.println("🎮 Welcome to UNO!");
        System.out.print("Enter number of players (2–4): ");
        int numPlayers = Integer.parseInt(scanner.nextLine());

        if (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Invalid number of players. Exiting...");
            scanner.close();
            return;
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Is player " + i + " a bot? (yes/no): ");
            String isBot = scanner.nextLine().trim().toLowerCase();

            System.out.print("Enter name for player " + i + ": ");
            String name = scanner.nextLine().trim();

            if (isBot.equals("yes")) {
                players.add(new Bot(name));
            } else {
                players.add(new Player(name));
            }
        }

        Game game = new Game(players);

        // Main game loop
        while (true) {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println("\nTop card: " + game.getTopCard());
            System.out.println(currentPlayer.getName() + "'s turn:");

            if (currentPlayer instanceof Bot) {
                ((Bot) currentPlayer).takeTurn(game);
            } else {
                List<Card> hand = currentPlayer.getHand();
                System.out.println("Your hand:");
                for (int i = 0; i < hand.size(); i++) {
                    System.out.println((i + 1) + ". " + hand.get(i));
                }
                System.out.println((hand.size() + 1) + ". Draw a card");

                int choice;
                while (true) {
                    System.out.print("Choose a card to play: ");
                    try {
                        choice = Integer.parseInt(scanner.nextLine()) - 1;
                        if (choice >= 0 && choice <= hand.size()) break;
                    } catch (NumberFormatException ignored) {}
                    System.out.println("Invalid choice.");
                }

                if (choice == hand.size()) {
                    game.drawCard(currentPlayer);
                } else {
                    Card chosen = hand.get(choice);
                    game.playTurn(currentPlayer, chosen);
                }
            }

            // Win check
            if (currentPlayer.hasWon()) {
                System.out.println("\n🏆 " + currentPlayer.getName() + " wins the game! 🎉");
                break;
            }

            // Move to next player
            game.nextPlayer();
        }

        scanner.close();
    }
}
