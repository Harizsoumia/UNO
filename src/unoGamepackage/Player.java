package unoGamepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a player in the UNO game
 */
public class Player {
    private final String name;
    private final List<Card> hand;
    
    /**
     * Constructor that initializes a player with a name
     * @param name The name of the player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }
    
    /**
     * Gets the player's name
     * @return The player's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Draws a card and adds it to the player's hand
     * @param card The card to draw
     */
    public void drawCard(Card card) {
        hand.add(card);
    }
    
    /**
     * Adds a card to the player's hand
     * @param card The card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }
    
    /**
     * Removes a card from the player's hand
     * @param card The card to remove
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    /**
     * Attempts to play a card
     * @param topCard The top card on the discard pile
     * @param chosenCard The card to play
     * @return An Optional containing the card if it can be played, or empty if it cannot
     */
    public Optional<Card> playCard(Card topCard, Card chosenCard) {
        if (hand.contains(chosenCard) && chosenCard.canBePlayedOn(topCard)) {
            hand.remove(chosenCard);
            return Optional.of(chosenCard);
        }
        return Optional.empty();
    }
    
    /**
     * Gets the player's hand
     * @return An unmodifiable list of cards in the player's hand
     */
    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }
    
    /**
     * Checks if the player has a valid move
     * @param topCard The top card on the discard pile
     * @return True if the player has a valid move, false otherwise
     */
    public boolean hasValidMove(Card topCard) {
        for (Card card : hand) {
            if (card.canBePlayedOn(topCard)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the number of cards in the player's hand
     * @return The number of cards
     */
    public int getHandSize() {
        return hand.size();
    }
    
    /**
     * Checks if the player has won
     * @return True if the player has won, false otherwise
     */
    public boolean hasWon() {
        return hand.isEmpty();
    }
}