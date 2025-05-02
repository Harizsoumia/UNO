package unoGamepackage;
public class ReverseCard extends ActionCard {

    public ReverseCard(Color color) {
        super(color);
    }

    @Override
    public String toString() {
        return color + " REVERSE";
    }
}

