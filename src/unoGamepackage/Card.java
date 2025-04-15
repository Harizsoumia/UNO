package unoGamepackage;

public abstract class Card {
    protected final Color color;

    public Card(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract boolean canBePlayedOn(Card topCard);

    public abstract void applyEffect(Game game,Player player);

    public abstract String toString();
}

