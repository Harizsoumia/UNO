package unoGamepackage;

public class SkipCard extends ActionCard {

    public SkipCard(Color color) {
        super(color);
    }


    @Override
    public String toString() {
        return color + " SKIP";
    }
}

