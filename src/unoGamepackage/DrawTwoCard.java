package unoGamepackage;

public class DrawTwoCard extends ActionCard {

    public DrawTwoCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Game game, Player player) {
        Player nextPlayer = game.getNextPlayer();
        game.drawCard(nextPlayer); // Next player draws two cards
        game.drawCard(nextPlayer);
        System.out.println(player.getName() + " played Draw Two!");
    }

    @Override
    public String toString() {
        return color + " DRAW TWO";
    }
}

