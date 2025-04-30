package unoGamepackage;
import java.awt.Graphics;

public abstract class Card {
    protected final Color color;
    protected Color effectiveColor; // For wild cards after color selection

    public Card(Color color) {
        this.color = color;
        this.effectiveColor = color;
    }

    public Color getColor() {
        return effectiveColor;
    }

    public Color getOriginalColor() {
        return color;
    }

    public void setEffectiveColor(Color color) {
        this.effectiveColor = color;
    }

    public abstract boolean canBePlayedOn(Card topCard);

    // Apply card effects when played
    public void applyEffect(Game game, Player player) {
        // Default implementation - no effect
        // Should be overridden by specific cards like Skip, Reverse, etc.
    }

    public abstract String toString();

    public String getImagePath() {
        return "images/" + color + "_" + this.getClass().getSimpleName() + ".png";
    }

    // For custom drawing
    public void draw(Graphics g, int x, int y, int width, int height) {
        // Drawing code here
    }
}  
