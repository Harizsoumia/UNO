package unoGamepackage;


public class WildCard extends ActionCard {

    public WildCard() {
        super(Color.WILD);
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

   
    @Override
    public String toString() {
        return "WILD";
    }
}

