package unoGamepackage;
public class ReverseCard extends ActionCard {

    public ReverseCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Game game, Player player) {
        game.reverseDirection(); // Reverses the turn direction
        System.out.println(player.getName() + " played Reverse!");
    }
    @Override
    public String toString() {
        return color + " REVERSE";
    }
}

