package unoGamepackage;

/**
 * Represents a Reverse card in UNO
 */
public class ReverseCard extends ActionCard {
    public ReverseCard(Color color) {
        super(color);
    }
    
    @Override
    public void applyEffect(Game game, Player player) {
        game.reverseDirection();
    }
    
    @Override
    public String toString() {
        return color.toString() + " Reverse";
    }
}