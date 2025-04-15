package unoGamepackage;

public abstract class ActionCard extends Card {

    public ActionCard(Color color) {
        super(color);
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return this.color == topCard.getColor() || topCard instanceof ActionCard;
    }
}

