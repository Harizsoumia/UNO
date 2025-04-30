package unoGamepackage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    /**
     * Constructor that initializes a standard UNO deck
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeCards();
        shuffle();
    }

    /**
     * Initializes the deck with standard UNO cards
     */
    private void initializeCards() {
        // Colors: Red, Blue, Green, Yellow
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

        for (Color color : colors) {
            // Add one 0 card
            cards.add(new NumberCard(color, 0));

            // Add two of each number cards 1-9
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(color, i));
                cards.add(new NumberCard(color, i));
            }

            // Add two of each action cards (Skip, Reverse, Draw Two)
            for (int i = 0; i < 2; i++) {
                cards.add(new SkipCard(color));
                cards.add(new ReverseCard(color));
                cards.add(new DrawTwoCard(color));
            }
        }

        // Add four wild cards and four wild draw four cards
        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard());
            cards.add(new WildDrawFourCard());
        }
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card from the deck
     * @return The drawn card
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            // If deck is empty, reshuffle the discard pile
            // In a real implementation, we'd need to get cards from the discard pile
            // For simplicity, we're creating a new deck
            initializeCards();
            shuffle();
        }

        return cards.remove(0);
    }

    /**
     * Adds a card to the bottom of the deck
     * @param card The card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Gets the number of cards in the deck
     * @return The number of cards
     */
    public int getSize() {
        return cards.size();
    }
} 
