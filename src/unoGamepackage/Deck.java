package unoGamepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
    	String[] colors = {"Red", "Yellow", "Green", "Blue"};
    	String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    	String[] actions = {"skip", "reverse", "draw 2"};


        // Cartes numérotées
        for (String color : colors) {
            // Une carte 0 par couleur
            cards.add(new Card (color, "0"));
            // Deux cartes de chaque nombre de 1 à 9 par couleur
            for (int i = 1; i <= 9; i++) {
                cards.add(new Card(color, String.valueOf(i)));
                cards.add(new Card(color, String.valueOf(i)));
            }
        }

        // Cartes spéciales
        for (String color : colors) {
            for (String action : actions) {
                // Deux cartes de chaque action par couleur
                cards.add(new Card(color, action));
                cards.add(new Card(color, action));
            }
        }

        // Cartes jokers
        for (int i = 0; i < 4; i++) {
            cards.add(new Card("Wild", "Wild"));
            cards.add(new Card("Wild", "Wild_draw_4"));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        } else {
            return null; // Le deck est vide
        }
    }

    public List<Card> getCards() {
        return cards;
    }
}
