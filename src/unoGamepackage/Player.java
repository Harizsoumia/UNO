package unoGamepackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    public enum PlayerType { HUMAN, BOT }
    private final String name;
    private PlayerType type;
    private final List<Card> hand;

    public Player(String name, PlayerType type) {
        this.name = name;
        this.type = type;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
public PlayerType getType() {
        return type;
    }

    public void drawCard(Player player) {
        if (!deck.isEmpty()) {
            Card drawn = deck.drawCard();
            player.addCard(drawn);
            notifyCardDrawn(player, drawn);
        } else {
            throw new IllegalStateException("Cannot draw from an empty deck.");
        }
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public Optional<Card> playCard(Card topCard, Card chosenCard) {
        if (hand.contains(chosenCard) && chosenCard.canBePlayedOn(topCard)) {
            hand.remove(chosenCard);
            return Optional.of(chosenCard);
        }
        return Optional.empty(); // No valid move
    }
    public boolean hasWon() {
        return hand.isEmpty();
    }



    // Add getter for hand
    public List<Card> getHand() {
        return Collections.unmodifiableList(hand); // Return unmodifiable view for safety
    }
    
    // Remove showCards() method with console output or modify it to return a string
    public String getCardsAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            sb.append(i).append(": ").append(hand.get(i)).append("\n");
        }
        return sb.toString();
    }
}
