package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the UNO game.
 */
public class Player {

    public enum PlayerType {
        HUMAN, BOT
    }

    private final String name;
    private final PlayerType type;
    private final ArrayList<Card> hand;  // Keep as a modifiable list.

    public Player(String name, PlayerType type) {
        this.name = (name == null || name.trim().isEmpty()) ? "Player" : name; // Default name
        this.type = type;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public PlayerType getType() {
        return type;
    }

    /**
     * Returns the player's hand. Now it's a modifiable list, allowing external modification.
     */
    public List<Card> getHand() {
        return hand;  // Return directly as modifiable list
    }

    /**
     * Returns the number of cards in the player's hand.
     */
    public int getHandSize() {
        return hand.size();
    }

    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    /**
     * Removes and returns the card at the specified index from the player's hand.
     * Returns null if the index is invalid.
     * @param index The index of the card to play.
     * @return The played Card object or null.
     */
    public Card playCard(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.remove(index);
        }
        return null;
    }

    /**
     * Removes the specific card object from the player's hand.
     * Useful if the view passes the Card object itself.
     * @param card The card object to remove.
     * @return true if the card was found and removed, false otherwise.
     */
    public boolean playCard(Card card) {
         return hand.remove(card);
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }

    /**
     * Finds the index of the first playable card in the hand against the top card.
     * Primarily for bot logic.
     * @param topCard The card on top of the discard pile.
     * @return The index of a valid card, or -1 if none found.
     */
    public int findValidCardIndex(Card topCard) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).canPlayOn(topCard)) {
                // Add check for Wild Draw Four rule if needed (can only play if no other card matches color)
                if (hand.get(i).getActualColor() == Card.Color.WILD && hand.get(i).getValue() == Card.Value.WILD_DRAW_FOUR) {
                    if (!canPlayWildDrawFour(topCard)) {
                        continue; // Cannot legally play WD4
                    }
                }
                return i;
            }
        }
        return -1; // No valid card found
    }

    /**
     * Checks if a Wild Draw Four can be legally played according to standard rules.
     * (Can only be played if the player has no other card matching the *current*
     * color of the discard pile).
     * @param topCard The card on top of the discard pile.
     * @return true if Wild Draw Four is a legal move, false otherwise.
     */
    public boolean canPlayWildDrawFour(Card topCard) {
        Card.Color requiredColor = topCard.getColor(); // Use the *effective* color
        for (Card cardInHand : hand) {
            if (cardInHand.getActualColor() != Card.Color.WILD && cardInHand.getColor() == requiredColor) {
                return false; // Found another card that matches the color, WD4 illegal
            }
        }
        return true; // No other card matches the color, WD4 is legal
    }

    /**
     * Simple bot logic to choose a color for a Wild card.
     * Chooses the color the bot has the most of (excluding Wilds).
     * @return The chosen Color.
     */
    public Card.Color chooseBotWildColor() {
        int[] colorCounts = new int[4]; // RED, BLUE, GREEN, YELLOW
        for (Card c : hand) {
            if (c.getActualColor() != Card.Color.WILD) {
                // Use ordinal() which relies on enum order: RED=0, BLUE=1, etc.
                colorCounts[c.getActualColor().ordinal()]++;
            }
        }

        int maxIndex = 0;
        int maxCount = -1; // Start lower than 0
        for (int i = 0; i < colorCounts.length; i++) {
            if (colorCounts[i] > maxCount) {
                maxCount = colorCounts[i];
                maxIndex = i;
            }
        }

        // If hand only had wilds, pick a random color (e.g., RED)
        if (maxCount <= 0) {
             return Card.Color.values()[new java.util.Random().nextInt(4)]; // Random color R,G,B,Y
        }
        return Card.Color.values()[maxIndex];
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
