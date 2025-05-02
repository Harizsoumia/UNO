package unoGamepackage;



/**
 * Interface for listening to game events.
 * Implement this interface to receive notifications about game state changes.
 */
public interface GameEventListener {
    /**
     * Called when a player plays a card
     * 
     * @param player The player who played the card
     * @param card The card that was played
     */
    void onCardPlayed(Player player, Card card);
    
    /**
     * Called when a move is invalid
     *
     * @param player The player who attempted the invalid move
     * @param card The card that couldn't be played
     */
    void onInvalidMove(Player player, Card card);
    
    /**
     * Called when a player is skipped
     *
     * @param player The player who was skipped
     */
    void onPlayerSkipped(Player player);
    
    /**
     * Called when the direction of play changes
     *
     * @param isReversed true if direction is counter-clockwise, false if clockwise
     */
    void onDirectionChanged(boolean isReversed);
    
    /**
     * Called when a player draws a card
     *
     * @param player The player who drew the card
     * @param card The card that was drawn
     */
    void onCardDrawn(Player player, Card card);
    
    /**
     * Called when the color is changed (usually after a wild card)
     *
     * @param newColor The new color chosen
     */
    void onColorChanged(CardColor newColor);
    
    /**
     * Called when a player wins the game
     *
     * @param winner The player who won
     */
    
    void onColorChangedRequest(Player player);
    
    private void notifyColorChangeRequest(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onColorChangedRequest(player);
        }
    }
    
    
    void onGameWon(Player winner);
}
