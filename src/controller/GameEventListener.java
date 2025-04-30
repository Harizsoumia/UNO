package controller;



import unoGamepackage.Card;
import unoGamepackage.Player;

/**
 * Interface for game event listeners.
 * This is used to notify the controller of game events.
 */
public interface GameEventListener {
	
	
	// In GameController interface
	void selectColor(unoGamepackage.Color color);
    /**
     * Called when a card is played
     * @param player The player who played the card
     * @param card The card that was played
     */
    void onCardPlayed(Player player, Card card);
    
    /**
     * Called when an invalid move is attempted
     * @param player The player who attempted the invalid move
     * @param card The card that was attempted to be played
     */
    void onInvalidMove(Player player, Card card);
    
    /**
     * Called when a card is drawn
     * @param player The player who drew the card
     * @param card The card that was drawn
     */
    void onCardDrawn(Player player, Card card);
    
    /**
     * Called when a player is skipped
     * @param player The player who was skipped
     */
    void onPlayerSkipped(Player player);
    
    /**
     * Called when the direction of play changes
     * @param isReversed Whether the direction is reversed
     */
    void onDirectionChanged(boolean isReversed);
    
    /**
     * Called when the game is won
     * @param winner The player who won the game
     */
    void onGameWon(Player winner);
    
    /**
     * Called when a color change is requested (for wild cards)
     * @param player The player who played the wild card
     */
    void onColorChangedRequest(Player player);
}