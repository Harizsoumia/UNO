package view;

import controller.GameController;
import unoGamepackage.Card;
import unoGamepackage.Color;
import unoGamepackage.Player;

import java.util.List;

/**
 * Interface defining the view component for the UNO game's MVC architecture.
 * Handles all UI elements and user interactions.
 */
public interface GameView {
    
    /**
     * Updates the displayed player hand with new cards
     * 
     * @param hand List of cards in the player's hand
     */
    void updatePlayerHand(List<Card> hand);
    
    /**
     * Updates the display of the top card on the discard pile
     * 
     * @param card The current top card
     */
    void updateTopCard(Card card);
    
    /**
     * Displays a message in the UI
     * 
     * @param message The message to display
     */
    void displayMessage(String message);
    
    /**
     * Enables the play button
     */
    void enablePlayButton();
    
    /**
     * Disables the play button
     */
    void disablePlayButton();
    
    /**
     * Enables the pass button
     */
    void enablePassButton();
    
    /**
     * Disables the pass button
     */
    void disablePassButton();
    
    /**
     * Updates the game status display
     * 
     * @param status The current game status text
     */
    void updateGameStatus(String status);
    
    /**
     * Sets the list of players in the game
     * 
     * @param players List of player names
     */
    void setPlayers(List<String> players);
    
    /**
     * Handles a card click event
     * 
     * @param card The card that was clicked
     */
    void onCardClicked(Card card);
    
    /**
     * Handles when the pass turn button is clicked
     */
    void onPassTurnClicked();
    
    /**
     * Sets the controller for this view
     * 
     * @param controller The game controller
     */
    void setController(GameController controller);
    
    /**
     * Updates the complete game state display
     * 
     * @param players List of all players
     * @param currentPlayerIndex Index of the current player
     * @param topCard The current top card
     * @param direction The current direction of play (positive = clockwise)
     * @param winner The winner of the game, or null if game is ongoing
     */
    void updateGameState(List<Player> players, int currentPlayerIndex, Card topCard, int direction, Player winner);
    
    /**
     * Shows that a player has played a card
     * 
     * @param player The player who played
     * @param card The card that was played
     */
    void showCardPlayed(Player player, Card card);
    
    /**
     * Shows that a player attempted an invalid move
     * 
     * @param player The player who attempted the move
     * @param card The card that could not be played
     */
    void showInvalidMove(Player player, Card card);
    
    /**
     * Shows that a player has drawn a card
     * 
     * @param player The player who drew a card
     * @param card The card that was drawn
     */
    void showCardDrawn(Player player, Card card);
    
    /**
     * Shows that a player's turn was skipped
     * 
     * @param player The player who was skipped
     */
    void showPlayerSkipped(Player player);
    
    /**
     * Shows that the direction of play has changed
     * 
     * @param isReversed True if direction is counter-clockwise, false if clockwise
     */
    void showDirectionChanged(boolean isReversed);
    
    /**
     * Shows the final result of the game
     * 
     * @param winner The player who won the game
     */
    void showGameResult(Player winner);
    
    /**
     * Prompts the user to select a color (for wild cards)
     */
    void promptColorSelection();
    
    /**
     * Shows that the wild card color has been changed
     * 
     * @param color The new color from unoGamepackage.Color enum
     */
    void showColorChanged(unoGamepackage.Color color);
}