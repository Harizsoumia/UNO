package unoGamepackage;

/**
 * Represents a Skip card in UNO
 */
public class SkipCard extends ActionCard {
    public SkipCard(Color color) {
        super(color);
    }
    
    @Override
    public void applyEffect(Game game, Player player) {
        game.skipNextPlayer();
    }
    
    @Override
    public String toString() {
        return color.toString() + " Skip";
    }
}