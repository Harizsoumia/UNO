package unoGamepackage;

/**
 * Represents a Wild card in UNO
 */
public class WildCard extends Card {
    public WildCard() {
        super(null); // No color initially
    }
    
    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true; // Can be played on any card
    }
    
    @Override
    public String toString() {
        return effectiveColor == null ? "Wild" : "Wild (" + effectiveColor + ")";
    }
    public void setEffectiveColor(Color color) {
        this.effectiveColor = color;
    }

}