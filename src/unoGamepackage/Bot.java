package unoGamepackage;

import java.util.HashMap;
import java.util.Map;

 // your custom enum
import unoGamepackage.Color;
/**
 * Bot player for the UNO game
 */
public class Bot extends Player {
    /**
     * Constructor that initializes a bot player with a name
     * @param name The name of the bot
     */
    public Bot(String name) {
        super(name);
    }
    
    /**
     * Gets the most common color in the bot's hand
     * @return The most common color
     */
    public Color getMostCommonColor() {
        Map<Color, Integer> colorCounts = new HashMap<>();
        
        // Count the occurrences of each color in hand
        for (Card card : getHand()) {
            Color cardColor = card.getOriginalColor();
            // Skip wild cards when counting colors
            if (cardColor != null) {
                colorCounts.put(cardColor, colorCounts.getOrDefault(cardColor, 0) + 1);
            }
        }
        
        // Find the most common color
        Color mostCommonColor = Color.RED; // Default
        int maxCount = 0;
        
        for (Map.Entry<Color, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonColor = entry.getKey();
            }
        }
        
        return mostCommonColor;
    }
    
    /**
     * Finds the best card to play
     * @param topCard The top card on the discard pile
     * @return The best card to play, or null if no playable card
     */
    public Card getBestCardToPlay(Card topCard) {
        Card bestCard = null;
        
        // First priority: action cards (Skip, Reverse, Draw Two)
        for (Card card : getHand()) {
            if (card.canBePlayedOn(topCard) && card instanceof ActionCard) {
                return card;
            }
        }
        
        // Second priority: wild cards
        for (Card card : getHand()) {
            if (card.canBePlayedOn(topCard) && card instanceof WildCard) {
                return card;
            }
        }
        
        // Third priority: number cards
        for (Card card : getHand()) {
            if (card.canBePlayedOn(topCard)) {
                return card;
            }
        }
        
        return null; // No playable card
    }
}