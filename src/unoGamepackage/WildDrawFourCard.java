package unoGamepackage;



public class WildDrawFourCard extends ActionCard {
        public WildDrawFourCard() {
            super(Color.WILD); // Wild cards do not have a color
        }

    @Override
    public String toString() {
        return "WILD DRAW FOUR";
    }
}

