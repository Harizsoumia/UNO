package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class UnoGame {
    private ArrayList<Player> players;
    private Deck deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex = 0;
    private boolean isClockwise;
    private Player winner;
    private Card lastPlayedCard;

    

    public void initializeGame(ArrayList<Player> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("Must have at least 2 players.");
        }

        this.players = players;
        this.deck = new Deck();
        this.discardPile = new Stack<>();
        this.currentPlayerIndex = 0;
        this.isClockwise = true;
        this.winner = null;

        for (Player p : players) {
            p.getHand().clear(); // Assumes hand is modifiable
        }

        dealInitialCards();
        startDiscardPile();
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }


    private void dealInitialCards() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());
            }
        }
    }

    private void startDiscardPile() {
        Card firstCard;
        do {
            if (deck.isEmpty()) {
                deck.reset();
            }

            firstCard = deck.drawCard();
            if (firstCard.getActualColor() == Card.Color.WILD &&
                firstCard.getValue() == Card.Value.WILD_DRAW_FOUR) {
                deck.addCards(Collections.singletonList(firstCard));
                deck.shuffle();
                firstCard = null;
            }
        } while (firstCard == null);

        discardPile.push(firstCard);
        lastPlayedCard = firstCard;

        applyFirstCardEffects(firstCard);
    }

    private void applyFirstCardEffects(Card card) {
        switch (card.getValue()) {
            case SKIP:
                moveToNextPlayer();
                break;
            case REVERSE:
                if (players.size() > 2) {
                    isClockwise = !isClockwise;
                    currentPlayerIndex = players.size() - 1;
                } else {
                    moveToNextPlayer();
                }
                break;
            case DRAW_TWO:
                drawCardsForPlayer(players.get(0), 2);
                moveToNextPlayer();
                break;
            case WILD:
                break;
            default:
                break;
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Card getTopCard() {
        return discardPile.peek();
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public Card playerDrawsCard(Player player) {
        if (player != getCurrentPlayer()) return null;

        if (deck.isEmpty() && !reshuffleDiscardPile()) {
            return null;
        }

        Card drawn = deck.drawCard();
        player.addCard(drawn);
        return drawn;
    }

    public Card playerPlaysCard(Player player, int cardIndex) {
        if (player != getCurrentPlayer() || isGameOver()) return null;

        if (cardIndex < 0 || cardIndex >= player.getHandSize()) return null;

        Card cardToPlay = player.getHand().get(cardIndex);
        if (!cardToPlay.canPlayOn(getTopCard())) return null;

        if (cardToPlay.getValue() == Card.Value.WILD_DRAW_FOUR &&
            !player.canPlayWildDrawFour(getTopCard())) {
            return null;
        }

        Card played = player.playCard(cardIndex);
        discardPile.push(played);
        lastPlayedCard = played;

        if (player.hasWon()) {
            winner = player;
            return played;
        }

        applyCardEffects(played);

        if (!isGameOver() && shouldAdvanceTurn(played)) {
            moveToNextPlayer();
        }

        return played;
    }

    private boolean shouldAdvanceTurn(Card card) {
        if (card.getValue() == Card.Value.SKIP ||
            card.getValue() == Card.Value.DRAW_TWO ||
            card.getValue() == Card.Value.WILD_DRAW_FOUR ||
            (card.getValue() == Card.Value.REVERSE && players.size() == 2)) {
            return false;
        }
        return true;
    }

    private void applyCardEffects(Card card) {
        switch (card.getValue()) {
            case SKIP:
                moveToNextPlayer();
                break;
            case REVERSE:
                if (players.size() > 2) {
                    isClockwise = !isClockwise;
                } else {
                    moveToNextPlayer();
                }
                break;
            case DRAW_TWO:
                moveToNextPlayer();
                drawCardsForPlayer(getCurrentPlayer(), 2);
                break;
            case WILD_DRAW_FOUR:
                moveToNextPlayer();
                drawCardsForPlayer(getCurrentPlayer(), 4);
                break;
            case WILD:
                // Color will be set externally via controller
                break;
            default:
                break;
        }
    }

    private void moveToNextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    private void drawCardsForPlayer(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (deck.isEmpty() && !reshuffleDiscardPile()) break;
            player.addCard(deck.drawCard());
        }
    }

    private boolean reshuffleDiscardPile() {
        if (discardPile.size() <= 1) return false;

        Card top = discardPile.pop(); // Keep top card
        List<Card> toReshuffle = new ArrayList<>(discardPile);
        discardPile.clear();
        discardPile.push(top);
        deck.addCards(toReshuffle);
        deck.shuffle();
        return true;
    }
    public Player getNextPlayer() {
        int direction = 1; // Assume the direction is clockwise. Use -1 for counterclockwise.
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        return players.get(nextIndex);
    }


    public boolean playCard(Card card) {
        Player current = getCurrentPlayer();
        Card top = getTopCard();

        if (card.canPlayOn(top)) {
            current.playCard(card); // Removes card from hand
            discardPile.push(card);

            if (card.isWild() && card.getActualColor() == Card.Color.WILD) {
                // Wild color must be chosen before this or defaulted
                card.setWildChosenColor(Card.Color.RED); // or let GUI set it
            }

            applyCardEffects(card);
            return true;
        } else {
            return false;
        }
    }
    public void startGame() {
        if (players == null || players.size() < 2) {
            throw new IllegalStateException("Game not initialized correctly.");
        }
    }
}
