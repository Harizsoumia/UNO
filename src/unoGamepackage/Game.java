package unoGamepackage;

import java.util.ArrayList;
import java.util.List;
import controller.GameEventListener;

/**
 * Represents an UNO game
 * Manages the core game logic, player turns, and game state
 */
public class Game {
    private final List<Player> players;
    private final Deck deck;
    private Card topCard;
    private int currentPlayerIndex;
    private int direction;  // 1 for clockwise, -1 for counter-clockwise
    private boolean gameOver;
    private Player winner;

    private final List<GameEventListener> eventListeners;

    /**
     * Constructor that initializes the game with a list of players
     * @param players The list of players
     */
    public Game(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.currentPlayerIndex = 0;
        this.direction = 1;  // Start clockwise
        this.gameOver = false;
        this.winner = null;
        this.eventListeners = new ArrayList<>();

        // Deal initial cards
        dealInitialCards();

        // Set the top card
        topCard = deck.drawCard();

        // If the top card is a wild card, set a random color
        if (topCard instanceof WildCard) {
            Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW}; // Exclude WILD from random selection
            int randomIndex = (int) (Math.random() * colors.length);
            ((WildCard) topCard).setEffectiveColor(colors[randomIndex]);
        }

        // If the top card is an action card, apply its effect
        if (topCard instanceof ActionCard) {
            topCard.applyEffect(this, null);
        }
    }

    private void dealInitialCards() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck.drawCard());
            }
        }
    }

    public void addGameEventListener(GameEventListener listener) {
        eventListeners.add(listener);
    }

    public void removeGameEventListener(GameEventListener listener) {
        eventListeners.remove(listener);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        int nextIndex = getNextPlayerIndex();
        return players.get(nextIndex);
    }

    private int getNextPlayerIndex() {
        int nextIndex = (currentPlayerIndex + direction) % players.size();
        if (nextIndex < 0) {
            nextIndex += players.size();
        }
        return nextIndex;
    }

    public void nextPlayer() {
        currentPlayerIndex = getNextPlayerIndex();
    }

    public void skipNextPlayer() {
        Player skippedPlayer = getNextPlayer();
        nextPlayer();
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerSkipped(skippedPlayer);
        }
    }

    public void reverseDirection() {
        direction *= -1;
        for (GameEventListener listener : eventListeners) {
            listener.onDirectionChanged(direction == -1);
        }
    }

    public boolean playTurn(Player player, Card card) {
        if (player != getCurrentPlayer()) {
            return false;
        }

        if (!card.canBePlayedOn(topCard)) {
            for (GameEventListener listener : eventListeners) {
                listener.onInvalidMove(player, card);
            }
            return false;
        }

        player.removeCard(card);
        topCard = card;
        
        for (GameEventListener listener : eventListeners) {
            listener.onCardPlayed(player, card);
        }

        // Apply card effects after notifying listeners about the card being played
        card.applyEffect(this, player);

        if (player.hasWon()) {
            gameOver = true;
            winner = player;
            for (GameEventListener listener : eventListeners) {
                listener.onGameWon(player);
            }
        }

        if (card instanceof WildCard) {
            for (GameEventListener listener : eventListeners) {
                listener.onColorChangedRequest(player);
            }
        }

        return true;
    }

    /**
     * Sets the color for a wild card using custom UNO Color enum
     * @param color The UNO color to set
     */
    public void setWildCardColor(Color color) {
        if (topCard instanceof WildCard) {
            ((WildCard) topCard).setEffectiveColor(color);
        }
    }

    public Card drawCard(Player player) {
        Card card = deck.drawCard();
        player.drawCard(card);

        for (GameEventListener listener : eventListeners) {
            listener.onCardDrawn(player, card);
        }

        return card;
    }

    public boolean checkUno() {
        Player currentPlayer = getCurrentPlayer();
        return currentPlayer.getHand().size() == 1;
    }

    public void passTurn() {
        // Just move to the next player
        nextPlayer();
    }

    public void playCard(Card card) {
        Player currentPlayer = getCurrentPlayer();
        playTurn(currentPlayer, card);
    }

    public boolean isCardPlayable(Card card) {
        return card.canBePlayedOn(topCard);
    }

    public Card getTopCard() {
        return topCard;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }
}