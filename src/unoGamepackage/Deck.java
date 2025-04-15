package unoGamepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        for (Color color : Color.values()) {
            if (color == Color.WILD) continue;

            // One 0 card per color
            cards.add(new NumberCard(color, 0));

            // Two of each 1–9
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(color, i));
                cards.add(new NumberCard(color, i));
            }

            // Two Skip, Reverse, Draw Two cards per color
            cards.add(new SkipCard(color));
            cards.add(new SkipCard(color));

            cards.add(new ReverseCard(color));
            cards.add(new ReverseCard(color));

            cards.add(new DrawTwoCard(color));
            cards.add(new DrawTwoCard(color));
        }

        // Wild cards (no color)
        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard());
            cards.add(new WildDrawFourCard());
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public void addCard(Card card) {
        addToBottom(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card drawCard() {
        if (isEmpty()) throw new IllegalStateException("The deck is empty.");
        return cards.remove(0);
    }

    public void addToBottom(Card card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }
}

