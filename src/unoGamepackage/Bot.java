package unoGamepackage;

import java.util.List;

public class Bot extends Player {

    public Bot(String name) {
        super(name);
    }

    public void takeTurn(Game game) {
        Card topCard = game.getTopCard();
        List<Card> hand = getHand();

        // Try to play a valid card
        for (Card card : hand) {
            if (card.canBePlayedOn(topCard)) {
                System.out.println(getName() + " plays: " + card);
                game.playTurn(this, card);
                return;
            }
        }

        // No valid card, so draw one
        System.out.println(getName() + " has no valid card. Drawing...");
        game.drawCard(this);
    }
}

