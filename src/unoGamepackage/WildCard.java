package unoGamepackage;

import java.util.Scanner;

public class WildCard extends ActionCard {

    public WildCard() {
        super(Color.WILD);
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public void applyEffect(Game game, Player player) {
        System.out.println(player.getName() + " played Wild Card!");
        
        // Let the player choose a new color
        // For simplicity, assume the color is chosen by user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose a color (RED, YELLOW, GREEN, BLUE): ");
        String chosenColor = scanner.nextLine().toUpperCase();
        
        while (!(chosenColor.equalsIgnoreCase("RED") || 
                chosenColor.equalsIgnoreCase("YELLOW") || 
                chosenColor.equalsIgnoreCase("BLUE") || 
                chosenColor.equalsIgnoreCase("GREEN"))) {
           System.out.println("Invalid color. Please choose RED, YELLOW, BLUE, or GREEN:");
           chosenColor = scanner.nextLine(); // make sure `scanner` is defined somewhere
       }

       // Once a valid color is entered, set the top card
       Color validColor = Color.valueOf(chosenColor.toUpperCase());
       game.setTopCard(new NumberCard(validColor, 0));
       System.out.println(player.getName() + " chose " + validColor + " as the new color.");

    }

    @Override
    public String toString() {
        return "WILD";
    }
}

