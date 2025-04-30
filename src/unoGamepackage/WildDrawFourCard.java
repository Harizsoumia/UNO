package unoGamepackage;

/**
 * Represents a Wild Draw Four card in UNO
 */
public class WildDrawFourCard extends WildCard {
    @Override
    public void applyEffect(Game game, Player player) {
        Player nextPlayer = game.getNextPlayer();
        for (int i = 0; i < 4; i++) {
            game.drawCard(nextPlayer);
        }
        game.skipNextPlayer();
    }
    
    @Override
    public String toString() {
        return effectiveColor == null ? "Wild Draw Four" : "Wild Draw Four (" + effectiveColor + ")";
    }
}