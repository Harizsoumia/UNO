package unoGamepackage;

public class NumberCard extends Card {
    private final int value;

    public NumberCard(Color color, int value) {
        super(color);
        this.value = value;
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return this.color == topCard.getColor() || topCard instanceof NumberCard && ((NumberCard) topCard).value == this.value;
    }

    @Override
    public void applyEffect(Game game, Player player) {
        // No effect for number cards
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}
