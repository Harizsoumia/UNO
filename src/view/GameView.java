import java.awt.Color;
import java.util.List;

public interface GameView {
    // Constants
    int FRAME_WIDTH = 900;
    int FRAME_HEIGHT = 700;
    int CARD_WIDTH = 70;
    int CARD_HEIGHT = 100;
    int FAN_ANGLE = 40;

    /**
     * Updates the discard pile with the given card
     * @param card The card to display on the discard pile
     */
    void updateDiscardPile(Card card);

    /**
     * Updates the background color based on the current card
     * @param cardLabel The JLabel representing the card
     */
    void updateBackgroundColorFromCard(JLabel cardLabel);

    /**
     * Converts a card color string to a Color object
     * @param cardColor The color string from the card
     * @return The corresponding Color object
     */
    Color getColorFromCardColor(String cardColor);

    /**
     * Creates a card label for display
     * @param card The card to display (null for card back)
     * @param displayType Either "face" or "dos" (back)
     * @return A JLabel representing the card
     */
    JLabel createCardLabel(Card card, String displayType);

    /**
     * Creates an icon for a card
     * @param value The card value
     * @param color The card color
     * @param type Either "face" or "dos" (back)
     * @return An Icon representing the card
     */
    Icon createCardIcon(String value, Color color, String type);

    /**
     * Sets up the layout of the game view
     */
    void setupLayout();

    /**
     * Sets up the center panel with draw and discard piles
     * @return The configured JPanel
     */
    JPanel setupCenterPanel();

    /**
     * Sets up a hand panel for a player
     * @param playerIndex The index of the player
     * @param numCards The number of cards in the hand
     * @return The configured JPanel
     */
    JPanel setupHandPanel(int playerIndex, int numCards);

    /**
     * Positions cards in a fan shape for display
     * @param pane The JLayeredPane to add cards to
     * @param cards The list of cards to display
     * @param playerIndex The index of the player
     */
    void positionCardsInFan(JLayeredPane pane, List<Card> cards, int playerIndex);

    /**
     * Sets the background color of the game panel
     * @param color The color to set
     */
    void setBackgroundColor(Color color);
}

// Supporting interfaces/classes that would need to be defined
interface Card {
    String getValue();
    String getColor();
}

interface Deck {
    Card drawCard();
}
