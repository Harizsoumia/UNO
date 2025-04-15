package unoGamepackage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    private final List<Player> players;
    private final Deck deck;
    private int currentPlayerIndex;
    private Card topCard;
    private int direction = 1; // 1 for clockwise, -1 for counter-clockwise

    public Game(List<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.currentPlayerIndex = 0;
        dealInitialCards();
        initializeTopCard();
    }

    private void dealInitialCards() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addCard(deck.drawCard());
            }
        }
    }

    public void setTopCard(Card card) {
        this.topCard = card;
    }

    private void initializeTopCard() {
        while (true) {
            Card card = deck.drawCard();
            if (!(card instanceof ActionCard)) {
                this.topCard = card;
                break;
            } else {
                deck.addCard(card); // Shuffle back if not valid
                deck.shuffle();
            }
        }
    }

    public Card getTopCard() {
        return topCard;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void playTurn(Player player, Card card) {
        if (card.canBePlayedOn(topCard)) {
            player.removeCard(card);
            topCard = card;
            card.applyEffect(this, player);
        } else {
            System.out.println("Invalid move! Card can't be played on " + topCard);
        }
    }

    public void drawCard(Player player) {
        Card drawn = deck.drawCard();
        System.out.println(player.getName() + " draws: " + drawn);
        player.addCard(drawn);
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    public void reverseDirection() {
        direction *= -1;
    }

    public void skipNextPlayer() {
        nextPlayer();
    }

    public Player getNextPlayer() {
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        return players.get(nextIndex);
    }
}
