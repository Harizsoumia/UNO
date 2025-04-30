package controller;

import view.GameView;
import unoGamepackage.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the UNO game, handles game logic and connects model with view
 */
public class GameController implements GameEventListener {
    private Game game;
    private final GameView view;
    private boolean isWaitingForColorSelection = false;

    /**
     * Constructor that initializes the game controller with the view
     * @param view The game view implementation
     */
    public GameController(GameView view) {
        this.view = view;
    }
    
    /**
     * Constructor that initializes the game controller with the view and model
     * @param view The game view implementation
     * @param game The game model
     */
    public GameController(GameView view, Game game) {
        this.view = view;
        this.game = game;
        this.game.addGameEventListener(this);
    }

    /**
     * Starts a new game with the specified players
     * @param humanPlayerNames List of human player names
     * @param botPlayerNames List of bot player names
     */
    public void startNewGame(List<String> humanPlayerNames, List<String> botPlayerNames) {
        List<Player> players = new ArrayList<>();
        
        // Create human players
        for (String name : humanPlayerNames) {
            players.add(new Player(name));
        }
        
        // Create bot players
        for (String name : botPlayerNames) {
            players.add(new Bot(name));
        }
        
        // Create and initialize game
        game = new Game(players);
        game.addGameEventListener(this);
        
        // Update view with initial game state
        updateViewGameState();
        
        // If current player is a bot, let it play
        if (getCurrentPlayer() instanceof Bot) {
            playBotTurn();
        }
    }
    
    /**
     * Starts a game with existing players
     */
    public void startGame() {
        // Update view with initial game state
        updateViewGameState();
        
        // If current player is a bot, let it play
        if (getCurrentPlayer() instanceof Bot) {
            playBotTurn();
        }
    }
    
    /**
     * Gets the current player
     * @return The current player
     */
    private Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }
    
    /**
     * Restarts the game with the same players
     */
    public void restartGame() {
        List<String> humanPlayerNames = new ArrayList<>();
        List<String> botPlayerNames = new ArrayList<>();
        
        for (Player player : game.getPlayers()) {
            if (player instanceof Bot) {
                botPlayerNames.add(player.getName());
            } else {
                humanPlayerNames.add(player.getName());
            }
        }
        
        startNewGame(humanPlayerNames, botPlayerNames);
    }
    
    /**
     * Updates the view with the current game state
     */
    private void updateViewGameState() {
        view.updateGameState(
            game.getPlayers(),
            game.getCurrentPlayerIndex(),
            game.getTopCard(),
            game.getDirection(),
            game.isGameOver() ? game.getWinner() : null
        );
    }

    /**
     * Plays a card from the current player's hand
     * @param playerIndex Index of the player
     * @param cardIndex Index of the card in the player's hand
     */
    public void playCard(int playerIndex, int cardIndex) {
        if (game.isGameOver() || isWaitingForColorSelection) {
            return;
        }
        
        Player player = game.getPlayers().get(playerIndex);
        if (player != game.getCurrentPlayer()) {
            return; // Not this player's turn
        }
        
        Card card = player.getHand().get(cardIndex);
        
        if (card.canBePlayedOn(game.getTopCard())) {
            game.playTurn(player, card);
            
            // If the card is a wild card, we need to wait for color selection
            if (card instanceof WildCard) {
                isWaitingForColorSelection = true;
                view.promptColorSelection();
                return;
            }
            
            // If the game is over, show the result
            if (game.isGameOver()) {
                view.showGameResult(game.getWinner());
                return;
            }
            
            // Move to next player
            game.nextPlayer();
            updateViewGameState();
            
            // If next player is a bot, let it play
            if (getCurrentPlayer() instanceof Bot) {
                playBotTurn();
            }
        } else {
            view.showInvalidMove(player, card);
        }
    }
    
    /**
     * Plays a card (overloaded method for direct card reference)
     * @param card The card to play
     */
    public void playCard(Card card) {
        int playerIndex = game.getCurrentPlayerIndex();
        Player player = game.getCurrentPlayer();
        int cardIndex = player.getHand().indexOf(card);
        
        if (cardIndex != -1) {
            playCard(playerIndex, cardIndex);
        }
    }
    
    /**
     * Checks if a card is playable
     * @param card The card to check
     * @return True if the card can be played, false otherwise
     */
    public boolean isCardPlayable(Card card) {
        return card.canBePlayedOn(game.getTopCard());
    }
    
    /**
     * Passes the turn to the next player
     */
    public void passTurn() {
        if (game.isGameOver() || isWaitingForColorSelection) {
            return;
        }
        
        // Move to next player
        game.nextPlayer();
        updateViewGameState();
        
        // If next player is a bot, let it play
        if (getCurrentPlayer() instanceof Bot) {
            playBotTurn();
        }
    }
    
    /**
     * Selects a color for a wild card
     * @param yellow The selected color
     */
    public void selectColor(unoGamepackage.Color yellow) {
        if (!isWaitingForColorSelection) {
            return;
        }
        
        unoGamepackage.Color unoColor = yellow; // Already correct

        
        // Set the color for the wild card
        game.setWildCardColor(unoColor);
        
        // Notify view about color change
        view.showColorChanged(yellow);
        
        // Reset waiting flag
        isWaitingForColorSelection = false;
        
        // Continue game
        game.nextPlayer();
        updateViewGameState();
        
        // If next player is a bot, let it play
        if (getCurrentPlayer() instanceof Bot) {
            playBotTurn();
        }
    }
    
    /**
     * Handles color selection for a bot
     * @param color The selected color
     */
    private void handleBotColorSelection(Color color) {
        unoGamepackage.Color unoColor = unoGamepackage.Color.fromAwtColor(color);
        game.setWildCardColor(unoColor);
        view.showColorChanged(color);
    }
    
    /**
     * Makes the current player draw a card
     */
    public void drawCard() {
        if (game.isGameOver() || isWaitingForColorSelection) {
            return;
        }
        
        Player currentPlayer = game.getCurrentPlayer();
        game.drawCard(currentPlayer);
        
        // Move to next player
        game.nextPlayer();
        updateViewGameState();
        
        // If next player is a bot, let it play
        if (getCurrentPlayer() instanceof Bot) {
            playBotTurn();
        }
    }
    
    /**
     * Checks if a player has UNO (one card left)
     * @return True if the current player has UNO, false otherwise
     */
    public boolean checkUno() {
        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer.getHand().size() == 1;
    }
    
    /**
     * Handles a bot's turn
     */
    private void playBotTurn() {
        if (game.isGameOver() || isWaitingForColorSelection) {
            return;
        }
        
        // Small delay to make the bot's actions visible
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                Bot bot = (Bot) getCurrentPlayer();
                Card topCard = game.getTopCard();
                
                // Try to find a playable card
                for (int i = 0; i < bot.getHand().size(); i++) {
                    Card card = bot.getHand().get(i);
                    if (card.canBePlayedOn(topCard)) {
                        // If it's a wild card, select a color
                        if (card instanceof WildCard) {
                            // Bot selects most common color in its hand
                            unoGamepackage.Color selectedColor = bot.getMostCommonColor();
                            // Play the card first
                            game.playTurn(bot, card);
                            // Then set the selected color
                            handleBotColorSelection(selectedColor.toAwtColor());
                        } else {
                            // Play a regular card
                            game.playTurn(bot, card);
                        }
                        
                        // If the game is over, show the result
                        if (game.isGameOver()) {
                            view.showGameResult(game.getWinner());
                            return;
                        }
                        
                        // Move to next player
                        game.nextPlayer();
                        updateViewGameState();
                        
                        // If next player is a bot, let it play
                        if (getCurrentPlayer() instanceof Bot) {
                            playBotTurn();
                        }
                        return;
                    }
                }
                
                // If no playable card, draw one
                game.drawCard(bot);
                
                // Move to next player
                game.nextPlayer();
                updateViewGameState();
                
                // If next player is a bot, let it play
                if (getCurrentPlayer() instanceof Bot) {
                    playBotTurn();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    // GameEventListener implementation
    
    @Override
    public void onCardPlayed(Player player, Card card) {
        view.showCardPlayed(player, card);
    }

    @Override
    public void onInvalidMove(Player player, Card card) {
        view.showInvalidMove(player, card);
    }

    @Override
    public void onCardDrawn(Player player, Card card) {
        view.showCardDrawn(player, card);
    }

    @Override
    public void onPlayerSkipped(Player player) {
        view.showPlayerSkipped(player);
    }

    @Override
    public void onDirectionChanged(boolean isReversed) {
        view.showDirectionChanged(isReversed);
    }

    @Override
    public void onGameWon(Player winner) {
        view.showGameResult(winner);
    }

    @Override
    public void onColorChangedRequest(Player player) {
        if (player instanceof Bot) {
            // Bot automatically selects a color
            Bot bot = (Bot) player;
            unoGamepackage.Color selectedColor = bot.getMostCommonColor();
            handleBotColorSelection(selectedColor.toAwtColor());
        } else {
            // Human needs to select a color
            isWaitingForColorSelection = true;
            view.promptColorSelection();
        }
    }
}