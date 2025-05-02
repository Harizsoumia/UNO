package unoGamepackage;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
public class Game {
    private final List<Player> players;
    private final Deck deck;
    private int currentPlayerIndex;
    private Card topCard;
    private int direction = 1; // 1 for clockwise, -1 for counter-clockwise
    private List<GameEventListener> eventListeners = new ArrayList<>();
    
    public void addGameEventListener(GameEventListener listener) {
        eventListeners.add(listener);
    }
    
    public void removeGameEventListener(GameEventListener listener) {
        eventListeners.remove(listener);
    }
    
    private void notifyCardPlayed(Player player, Card card) {
        for (GameEventListener listener : eventListeners) {
            listener.onCardPlayed(player, card);
        }
    }
    
    public Game(List<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.currentPlayerIndex = 0;
        dealInitialCards();
        initializeTopCard();
    }

    private void dealInitialCards() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addCard(deck.drawCard());
            }
        }
    }

    public void setTopCard(Card card) {
        this.topCard = card;
    }

    private void initializeTopCard() {
        while (true) {
            Card card = deck.drawCard();
            if (!(card instanceof ActionCard)) {
                this.topCard = card;
                break;
            } else {
                deck.addCard(card); // Shuffle back if not valid
                deck.shuffle();
            }
        }
    }

    public Card getTopCard() {
        return topCard;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void playTurn(Player player, Card card) {
        if (card.canBePlayedOn(topCard)) {
            player.removeCard(card);
            topCard = card;
            notifyCardPlayed(player, card); 
            card.applyEffect(this, player);
        } else {
        	notifyInvalidMove(player, card);        }
    }

   
    
    public void drawCard(Player player) {
        Card drawn = deck.drawCard();
        player.drawCard(drawn);
        notifyCardDrawn(player, drawn);
    }
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    public void reverseDirection() {
        direction *= -1;
        notifyDirectionChanged(direction == -1); 
    }

    public void skipNextPlayer() {
        Player skipped = getNextPlayer();
        nextPlayer();
        notifyPlayerSkipped(skipped);  // Create and use this method
    }

    public Player getNextPlayer() {
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        return players.get(nextIndex);
    }
    // notifications
    private void notifyInvalidMove(Player player, Card card) {
        for (GameEventListener listener : eventListeners) {
            listener.onInvalidMove(player, card);
        }
    }

    private void notifyCardDrawn(Player player, Card card) {
        for (GameEventListener listener : eventListeners) {
            listener.onCardDrawn(player, card);
        }
    }

    private void notifyPlayerSkipped(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerSkipped(player);
        }
    }

    private void notifyDirectionChanged(boolean isReversed) {
        for (GameEventListener listener : eventListeners) {
            listener.onDirectionChanged(isReversed);
        }
    }
    
    //Add game state methods for UI:
    
    public int getDirection() {
        return direction;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Player getWinner() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }
        return null;
    }
    
    public void handleCardEffect(Card card, Player player) {
        if (card instanceof ReverseCard) {
            // Reverse play direction
            reverseDirection();
            notifyDirectionChanged(direction == -1);
        } else if (card instanceof SkipCard) {
            // Skip the next player
            skipNextPlayer();
        } else if (card instanceof WildCard) {
            // Notify UI to request a color selection
            notifyColorChangeRequest(player);
        } else if (card instanceof WildDrawFourCard) {
            // Next player must draw four cards
            Player nextPlayer = getNextPlayer();
            for (int i = 0; i < 4; i++) {
                drawCard(nextPlayer);
            }
            notifyColorChangeRequest(player);
        }

        // Notify that a card was played
        notifyCardPlayed(player, card);

        // Check if the game has ended
        if (isGameOver()) {
            notifyGameWon(getWinner());
        }
    }
}
