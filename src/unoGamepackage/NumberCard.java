package unoGamepackage;

/**
 * Represents a number card in UNO
 */
public class NumberCard extends Card {
    private final int number;
    
    public NumberCard(Color color, int number) {
        super(color);
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }
    
    @Override
    public boolean canBePlayedOn(Card topCard) {
        if (topCard instanceof NumberCard) {
            return topCard.getColor() == this.color || ((NumberCard) topCard).getNumber() == this.number;
        }
        return topCard.getColor() == this.color;
    }
    
    @Override
    public String toString() {
        return color.toString() + " " + number;
    }
}