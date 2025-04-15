package unoGamepackage;

import java.util.Scanner;

public class WildDrawFourCard extends ActionCard {
        public WildDrawFourCard() {
            super(Color.WILD); // Wild cards do not have a color
        }

        @Override
        public void applyEffect(Game game, Player player) {
            System.out.println(player.getName() + " played Wild Draw Four!");

            // The next player must draw four cards
            Player nextPlayer = game.getNextPlayer();
            System.out.println(nextPlayer.getName() + " must draw 4 cards!");
            for (int i = 0; i < 4; i++) {
                game.drawCard(nextPlayer);
            }

            // Let the player choose the new color
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
           public boolean canBePlayedOn(Card topCard) {
               return true;
           }


    @Override
    public String toString() {
        return "WILD DRAW FOUR";
    }
}

