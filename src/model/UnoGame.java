package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Manages the overall state and logic of the UNO game.
 */
public class UnoGame {
    private ArrayList<Player> players;
    private Deck deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private boolean isClockwise;
    private Player winner;
    private Card lastPlayedCard; // Keep track for wild color setting


    public UnoGame() {
        // Initialized when game starts
    }

    /**
     * Sets up a new game with the given players.
     * Deals cards and sets up the initial discard pile.
     * @param players List of Player objects participating.
     */
    public void initializeGame(ArrayList<Player> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("Must have at least 2 players.");
        }
        this.players = players;
        this.deck = new Deck(); // Create and shuffle a new deck
        this.discardPile = new Stack<>();
        this.currentPlayerIndex = 0; // Or a random starting player
        this.isClockwise = true;
        this.winner = null;
        this.lastPlayedCard = null;


        // Clear hands if players are reused from a previous game
        for (Player p : players) {
             p.getHand().clear(); // Need to modify Player.getHand if it returns unmodifiable list
             // If getHand returns unmodifiable: Need a clearHand() method in Player
        }


        dealInitialCards();
        startDiscardPile();
    }

    private void dealInitialCards() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());
            }
        }
    }

     /**
      * Draws the first card for the discard pile. Handles reshuffling and
      * special first card rules (e.g., no Wild Draw Four).
      */
     private void startDiscardPile() {
        Card firstCard = null;
        do {
             if (deck.isEmpty()) {
                 // Extremely unlikely with a fresh deck, but handle anyway
                 deck.reset();
             }
             firstCard = deck.drawCard();

             // Rule: If first card is Wild Draw Four, return to deck and shuffle
             if (firstCard.getActualColor() == Card.Color.WILD && firstCard.getValue() == Card.Value.WILD_DRAW_FOUR) {
                 deck.addCards(Collections.singletonList(firstCard)); // Add it back
                 deck.shuffle();
                 firstCard = null; // Force redraw
             }
         } while (firstCard == null);


         discardPile.push(firstCard);
         lastPlayedCard = firstCard;


         // Apply effects of the first card immediately (Skip, Reverse, Draw Two, Wild)
         System.out.println("First card is: " + firstCard);
         applyFirstCardEffects(firstCard);
     }


     /**
      * Applies effects for the very first card turned over.
      * Adapted from applyCardEffects for the start of the game.
      * @param card The first card on the discard pile.
      */
      private void applyFirstCardEffects(Card card) {
          // Note: Assumes the card is already on the discard pile.
          switch (card.getValue()) {
              case SKIP:
                  System.out.println("First card effect: Player 1 is skipped.");
                  moveToNextPlayer(); // Skip the first player
                  break;
              case REVERSE:
                   System.out.println("First card effect: Direction reversed.");
                  if (players.size() > 2) { // Reverse only effective with > 2 players
                      isClockwise = !isClockwise;
                       // The current player becomes the *last* player in the new direction
                       currentPlayerIndex = players.size() -1;
                  } else { // With 2 players, reverse acts like skip
                       System.out.println(" (Acts like Skip with 2 players)");
                       moveToNextPlayer();
                  }
                  break;
              case DRAW_TWO:
                  System.out.println("First card effect: Player 1 draws 2 and is skipped.");
                  Player firstPlayer = players.get(0);
                  drawCardsForPlayer(firstPlayer, 2);
                  moveToNextPlayer(); // Skip the first player
                  break;
              case WILD:
                  System.out.println("First card effect: Player 1 chooses the color.");
                  // The current player (player 0) needs to choose the color.
                   // This requires interaction *before* their actual turn starts.
                   // The GUI controller must handle this specific state.
                   // For now, we can't automatically set color here.
                   // A flag or specific game state might be needed.
                   // Simplification: For now, let the game proceed, player 1 can play anything.
                  break;
               case WILD_DRAW_FOUR:
                   // Should not happen due to startDiscardPile logic
                   System.err.println("ERROR: Wild Draw Four was the first card!");
                   break;
              default:
                  // No special effect on the first turn
                  break;
          }
      }


    // --- Getters for Game State ---

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        if (players == null || players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }

     public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
     }

    public Card getTopCard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
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

    // --- Core Game Actions (called by Controller/View) ---

    /**
     * Handles the current player drawing a card.
     * Includes logic for reshuffling the discard pile if the deck is empty.
     * @param player The player who is drawing.
     * @return The card drawn, or null if no cards could be drawn.
     */
    public Card playerDrawsCard(Player player) {
         if (player != getCurrentPlayer()) {
              System.err.println("Warning: Non-current player trying to draw.");
              // Optionally throw an exception or return null
              return null;
         }


        if (deck.isEmpty()) {
            if (!reshuffleDiscardPile()) {
                // No cards left anywhere - should be impossible in standard UNO
                System.err.println("Error: No cards left in deck or discard pile!");
                // End game? Declare draw?
                 // For now, return null indicating failure to draw.
                 return null;
            }
        }
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            player.addCard(drawnCard);
        }
        return drawnCard;
    }

    /**
     * Attempts to play the card at the given index from the player's hand.
     * Handles validity checks, updates discard pile, applies effects, checks for win.
     * @param player The player attempting to play.
     * @param cardIndex The index of the card in the player's hand.
     * @return The Card object that was played, or null if the move was invalid.
     */
    public Card playerPlaysCard(Player player, int cardIndex) {
         if (player != getCurrentPlayer() || isGameOver()) {
              return null; // Not their turn or game over
         }


         if (cardIndex < 0 || cardIndex >= player.getHandSize()) {
              return null; // Invalid index
         }


         Card cardToPlay = player.getHand().get(cardIndex); // Peek first
         Card topCard = getTopCard();


         if (cardToPlay.canPlayOn(topCard)) {
              // Specific check for Wild Draw Four legality
              if (cardToPlay.getActualColor() == Card.Color.WILD && cardToPlay.getValue() == Card.Value.WILD_DRAW_FOUR) {
                   if (!player.canPlayWildDrawFour(topCard)) { // Use player's check method
                        return null; // Illegal WD4 play
                   }
              }


              // Valid move - remove from hand *before* applying effects
              Card playedCard = player.playCard(cardIndex); // Actually remove
              if (playedCard != null) {
                   discardPile.push(playedCard);
                   lastPlayedCard = playedCard; // Track last played for wild color


                   // Important: Check for win *after* playing the card
                   if (player.hasWon()) {
                        winner = player;
                        // Don't apply effects or move to next player if game is won
                        return playedCard;
                   }


                   // Apply effects *before* moving to the next player
                   applyCardEffects(playedCard);


                   // If the game didn't end, and effects didn't skip multiple turns, advance
                   if (!isGameOver()) {
                        // Don't advance turn if the effect already did (like Draw Two/Four, Skip)
                        if (playedCard.getValue() != Card.Value.SKIP &&
                            playedCard.getValue() != Card.Value.DRAW_TWO &&
                            playedCard.getValue() != Card.Value.WILD_DRAW_FOUR &&
                            !(playedCard.getValue() == Card.Value.REVERSE && players.size() == 2)) // Reverse acts like skip for 2 players
                         {
                             moveToNextPlayer();
                         }
                   }
                   return playedCard;
              }
         }


         return null; // Card cannot be played
     }


     /**
      * Applies the effects of a played card (Skip, Reverse, Draw).
      * Assumes the card is already on the discard pile.
      * @param playedCard The card whose effects need to be applied.
      */
      private void applyCardEffects(Card playedCard) {
           Player playerToAffect;


           switch (playedCard.getValue()) {
               case SKIP:
                    System.out.println("Effect: Skipping next player.");
                   moveToNextPlayer(); // Skip the player who would have been next
                   break;


               case REVERSE:
                   System.out.println("Effect: Reversing direction.");
                    if (players.size() > 2) {
                       isClockwise = !isClockwise;
                    } else { // Acts like Skip for 2 players
                       System.out.println(" (Acts like Skip with 2 players)");
                       moveToNextPlayer();
                    }
                   break;


               case DRAW_TWO:
                   moveToNextPlayer(); // Determine who is next first
                   playerToAffect = getCurrentPlayer(); // This player draws
                    System.out.println("Effect: " + playerToAffect.getName() + " draws 2 and is skipped.");
                   drawCardsForPlayer(playerToAffect, 2);
                   // Turn automatically moves past the affected player now
                   break;


               case WILD_DRAW_FOUR:
                    // Player playing WD4 must choose color (handled by Controller/View calling setWildColor)
                   moveToNextPlayer(); // Determine who is next first
                   playerToAffect = getCurrentPlayer(); // This player draws
                   System.out.println("Effect: " + playerToAffect.getName() + " draws 4 and is skipped.");
                   drawCardsForPlayer(playerToAffect, 4);
                    // Turn automatically moves past the affected player now
                   break;


               case WILD:
                    // Player playing WILD must choose color (handled by Controller/View calling setWildColor)
                   System.out.println("Effect: Player chose color (needs to be set).");
                   break;


               default:
                   // No effect
                   break;
           }
      }


    /**
     * Sets the chosen color for the most recently played wild card.
     * Should be called by the Controller/View after a player chooses.
     * @param color The chosen Color (RED, GREEN, BLUE, YELLOW).
     */
    public void setWildColor(Card.Color color) {
        if (lastPlayedCard != null && lastPlayedCard.getActualColor() == Card.Color.WILD) {
             if (color != Card.Color.WILD) { // Can only choose non-wild colors
                lastPlayedCard.setWildChosenColor(color);
                 System.out.println("Wild color set to: " + color);
             }
        }
    }


    /**
     * Moves the turn to the next player based on the current direction.
     */
    public void moveToNextPlayer() {
        if (players.isEmpty()) return;
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
         System.out.println("--> Next turn: " + getCurrentPlayer().getName());
    }


    // --- Helper Methods ---

    /**
     * Takes cards from the discard pile, adds them to the deck, and shuffles.
     * Leaves the top card on the discard pile.
     * @return true if reshuffle was successful, false if no cards to reshuffle.
     */
    private boolean reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            return false; // Cannot reshuffle with 0 or 1 card in discard
        }
        System.out.println("Reshuffling discard pile into deck...");
        Card topCard = discardPile.pop(); // Keep the top card
        deck.addCards(discardPile);    // Add the rest to the deck
        discardPile.clear();             // Clear the stack
        discardPile.push(topCard);       // Put the top card back
        deck.shuffle();
        return true;
    }


     /**
     * Helper method to draw a specified number of cards for a player.
     * Handles deck reshuffling if necessary.
     * @param player The player to draw cards.
     * @param count The number of cards to draw.
     */
    private void drawCardsForPlayer(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (deck.isEmpty()) {
                if (!reshuffleDiscardPile()) {
                     System.err.println("Error: Could not draw card - deck and discard empty.");
                    break; // Stop drawing if no cards available
                }
            }
             Card drawn = deck.drawCard();
             if (drawn != null) {
                 player.addCard(drawn);
             } else {
                  System.err.println("Error: Drew null card after reshuffle check.");
                  break; // Should not happen
             }
        }
    }
}