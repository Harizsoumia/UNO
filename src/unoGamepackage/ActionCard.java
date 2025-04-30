package unoGamepackage;

/**
 * Abstract class for action cards
 */
public abstract class ActionCard extends Card {
    public ActionCard(Color color) {
        super(color);
    }
    
    @Override
    public boolean canBePlayedOn(Card topCard) {
        return topCard.getColor() == this.color || 
               topCard instanceof ActionCard && ((ActionCard) topCard).getClass() == this.getClass();
    }
}