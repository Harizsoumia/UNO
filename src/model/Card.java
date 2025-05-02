package model;

import java.util.Objects;

/**
 * Represents a single UNO card with a color and a value.
 */
public class Card {

    public enum Color {
        RED, BLUE, GREEN, YELLOW, WILD
    }

    public enum Value {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
    }

    private Color color;
    private Value value;
    private Color wildChosenColor = null; // Store chosen color for wild cards after play

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        // If a wild color was chosen, return that, otherwise the card's base color
        return wildChosenColor != null ? wildChosenColor : color;
    }

    public Value getValue() {
        return value;
    }

    // Used by UnoGame to set the effective color after a wild card is played
    public void setWildChosenColor(Color wildChosenColor) {
        if (this.color == Color.WILD) {
            this.wildChosenColor = wildChosenColor;
        }
    }

    // Resets the wild chosen color (e.g., when card goes back to deck/hand)
    public void resetWildColor() {
        this.wildChosenColor = null;
    }

    // Gets the original color, ignoring chosen wild color
    public Color getActualColor() {
        return this.color;
    }


    /**
     * Checks if this card can be legally played on top of the given card.
     * Takes into account the chosen color for wild cards on the discard pile.
     * @param topCard The card currently on top of the discard pile.
     * @return true if this card can be played, false otherwise.
     */
    public boolean canPlayOn(Card topCard) {
        if (topCard == null) { // Should not happen in normal play after first card
           return true;
        }
        // Wild cards can always be played (rules might vary on Wild Draw 4, checked in game logic)
        if (this.getActualColor() == Color.WILD) {
            return true;
        }
        // Match color (considering the chosen wild color of the top card)
        if (this.getColor() == topCard.getColor()) {
            return true;
        }
        // Match value (Numbers, Skip, Reverse, Draw Two)
        if (this.getValue() == topCard.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
         if (wildChosenColor != null) {
             return wildChosenColor + " " + value + " (WILD)";
         }
         return color + " " + value;
    }

    // Optional: equals and hashCode if needed for Set operations or specific list searches
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        // Note: Doesn't compare wildChosenColor as it's transient state
        return color == card.color && value == card.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value);
    }
}