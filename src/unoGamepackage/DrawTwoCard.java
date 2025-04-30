package unoGamepackage;

/**
 * Represents a Draw Two card in UNO
 */
public class DrawTwoCard extends ActionCard {
    public DrawTwoCard(Color color) {
        super(color);
    }
    
    @Override
    public void applyEffect(Game game, Player player) {
        Player nextPlayer = game.getNextPlayer();
        for (int i = 0; i < 2; i++) {
            game.drawCard(nextPlayer);
        }
        game.skipNextPlayer();
    }
    
    @Override
    public String toString() {
        return color.toString() + " Draw Two";
    }
}