package model;

import java.util.Collections;
import java.util.Stack;
import java.util.List;

/**
 * Represents the draw pile and handles deck creation and shuffling.
 */
public class Deck {
    private Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        initializeDeck();
    }

    private void initializeDeck() {
        cards.clear(); // Ensure deck is empty before initializing

        // Non-wild cards
        for (Card.Color color : new Card.Color[]{Card.Color.RED, Card.Color.BLUE, Card.Color.GREEN, Card.Color.YELLOW}) {
            // One Zero card
            cards.push(new Card(color, Card.Value.ZERO));

            // Two of each number card 1-9
            for (Card.Value value : new Card.Value[]{Card.Value.ONE, Card.Value.TWO, Card.Value.THREE, Card.Value.FOUR, Card.Value.FIVE, Card.Value.SIX, Card.Value.SEVEN, Card.Value.EIGHT, Card.Value.NINE}) {
                cards.push(new Card(color, value));
                cards.push(new Card(color, value));
            }

            // Two of each action card
            for (Card.Value value : new Card.Value[]{Card.Value.SKIP, Card.Value.REVERSE, Card.Value.DRAW_TWO}) {
                cards.push(new Card(color, value));
                cards.push(new Card(color, value));
            }
        }

        // Wild cards
        for (int i = 0; i < 4; i++) {
            cards.push(new Card(Card.Color.WILD, Card.Value.WILD));
            cards.push(new Card(Card.Color.WILD, Card.Value.WILD_DRAW_FOUR));
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (isEmpty()) {
            return null; // Let UnoGame handle reshuffling
        }
        return cards.pop();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Adds a collection of cards to the deck (used for reshuffling).
     * Ensures cards have their wild state reset.
     * @param cardsToAdd List of cards from the discard pile.
     */
    public void addCards(List<Card> cardsToAdd) {
        for(Card card : cardsToAdd) {
             card.resetWildColor(); // Reset chosen wild color before adding back
             cards.push(card);
        }
    }

    // Re-initialize the deck completely (e.g., for a new game)
    public void reset() {
        initializeDeck();
    }
}