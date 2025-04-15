package unoGamepackage;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void drawCard(Deck deck) {
        if (!deck.isEmpty()) {
            hand.add(deck.drawCard());
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

    public Card playCard(Card topCard, Card chosenCard) {
        if (hand.contains(chosenCard) && chosenCard.canBePlayedOn(topCard)) {
            hand.remove(chosenCard);
            return chosenCard;
        }
        return null; // Invalid move
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }
}
