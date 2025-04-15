package unoGamepackage;

public class SkipCard extends ActionCard {

    public SkipCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Game game, Player player) {
        game.skipNextPlayer(); // Skips the next player
        System.out.println(player.getName() + " played Skip!");
    }

    @Override
    public String toString() {
        return color + " SKIP";
    }
}

