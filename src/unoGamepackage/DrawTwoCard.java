package unoGamepackage;

public class DrawTwoCard extends ActionCard {

    public DrawTwoCard(Color color) {
        super(color);
    }


    @Override
    public String toString() {
        return color + " DRAW TWO";
    }
}

