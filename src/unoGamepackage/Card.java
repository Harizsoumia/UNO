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

    public abstract String toString();
    public String getImagePath() {
        return "images/" + color + "_" + value + ".png";
    }
    
    // For custom drawing
    public void draw(Graphics g, int x, int y, int width, int height) {
        // Drawing code here
    }
}

