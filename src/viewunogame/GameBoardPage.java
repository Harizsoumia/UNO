 package viewunogame;



import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import model.Card;

import model.Player;

import model.UnoGame;

import view.*;

import java.util.List;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;



public class GameBoardPage extends CustomPanel {



private UnoGame game;

private List<Player> players;

private Player currentPlayer;

private int currentPlayerIndex;



// UI Components

private CustomPanel northPanel;

private CustomPanel centerPanel;

private CustomPanel southPanel;

private CustomPanel eastPanel;

private CustomPanel westPanel;



private CustomLabel currentPlayerLabel;

private CustomImageLabel currentPlayerIcon;



private CardComponent discardPileComponent;

private CardComponent drawPileComponent;



private CustomPanel handPanel;

private CustomPanel[] playerPanels;

private CustomLabel[] playerNameLabels;



private CustomButton unoButton;

private boolean unoButtonEnabled = true;



// Constructor

public GameBoardPage(UnoGame game) {

this.game = game;

this.players = game.getPlayers();

this.currentPlayerIndex = game.getCurrentPlayerIndex();

this.currentPlayer = players.get(currentPlayerIndex);



setLayout(new BorderLayout());

setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

initComponents();

updateDisplay();

}



/**

* Initialize all UI components

*/

private void initComponents() {

// Center panel - Discard and Draw piles

centerPanel = createCenterPanel();

add(centerPanel, BorderLayout.CENTER);


// Initialize player panels based on number of players

initPlayerPanels();


// North panel - Current player info

northPanel = createNorthPanel();

add(northPanel, BorderLayout.NORTH);

}



/**

* Creates the north panel with current player info

*/

private CustomPanel createNorthPanel() {

CustomPanel contentPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));


currentPlayerLabel = new CustomLabel();

currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 24));

currentPlayerLabel.setForeground(Color.WHITE);

contentPanel.add(currentPlayerLabel);


currentPlayerIcon = new CustomImageLabel();

contentPanel.add(currentPlayerIcon);


unoButton = new CustomButton("UNO!");

unoButton.setFont(new Font("Arial", Font.BOLD, 16));

unoButton.setPreferredSize(new Dimension(80, 40));

unoButton.addMouseListener(new MouseAdapter() {

@Override

public void mouseClicked(MouseEvent e) {

handleUnoButtonClick();

}

});

contentPanel.add(unoButton);


ScrollPaneWrapper scrollPane = new ScrollPaneWrapper(contentPanel);

scrollPane.setHorizontalScrollBarPolicy(ScrollPaneWrapper.HORIZONTAL_SCROLLBAR_AS_NEEDED);

scrollPane.setVerticalScrollBarPolicy(ScrollPaneWrapper.VERTICAL_SCROLLBAR_NEVER);

scrollPane.setBorder(null);


CustomPanel panel = new CustomPanel(new BorderLayout());

panel.add(scrollPane, BorderLayout.CENTER);


return panel;

}




/**

* Creates the center panel with discard pile, draw pile

*/

private CustomPanel createCenterPanel() {
    CustomPanel panel = new CustomPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));

    // Discard pile
    Card topCard = game.getTopCard();
    discardPileComponent = new CardComponent(topCard);
    discardPileComponent.setPreferredSize(new Dimension(120, 180));
   
    discardPileComponent.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            GameBoardPage.this.handleDrawCard(); // 👈 fix here
        }
    });

    CustomLabel discardLabel = new CustomLabel("Discard Pile");
    discardLabel.setForeground(Color.WHITE);

    CustomPanel discardPanel = new CustomPanel();
    discardPanel.setLayout(new BoxLayout(discardPanel, BoxLayout.Y_AXIS));
    discardPanel.add(discardPileComponent);
    discardPanel.add(Box.createVerticalStrut(10));
    discardPanel.add(discardLabel);

    panel.add(discardPanel);

    // Draw pile
    drawPileComponent = new CardComponent(null, true); // Using the back of the card
    drawPileComponent.setPreferredSize(new Dimension(120, 180));
    drawPileComponent.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            handleDrawCard();
        }

        private void handleDrawCard() {
            // Only allow the current player to draw cards
            if (currentPlayer.getType() != Player.PlayerType.HUMAN) {
                return;
            }

            Card drawnCard = game.playerDrawsCard(currentPlayer);

            if (drawnCard != null) {
                currentPlayer.addCard(drawnCard);
                showFloatingText(currentPlayer.getName() + " drew a card");

                // Update the display to show the new card
                updateDisplay();

                // Check if the drawn card can be played immediately
                Card topCard = game.getTopCard();

                if (drawnCard.canPlayOn(topCard)) {
                    // Ask if player wants to play the drawn card
                    int response = JOptionPane.showConfirmDialog(
                    	    centerPanel, // or any visible component in your interface
                    	    "Do you want to play the drawn card (" + drawnCard + ")?",
                    	    "Play Drawn Card",
                    	    JOptionPane.YES_NO_OPTION
                    	);

                    if (response == JOptionPane.YES_OPTION) {
                        // Remove the card from hand
                        for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                            Card c = currentPlayer.getHand().get(i);
                            if (c.equals(drawnCard)) {
                                currentPlayer.getHand().remove(i);
                                break;
                            }
                        }

                        // Handle wild card color selection
                        if (drawnCard.getColor() == Card.Color.WILD) {
                            showColorPicker(drawnCard);
                        }

                        // Play the card
                        game.playCard(drawnCard);

                        // Show effect text for special cards
                        if (isSpecialCard(drawnCard)) {
                            showFloatingText(currentPlayer.getName() + " played " + drawnCard.getValue() + "!");
                        }

                        // Check for win condition
                        if (currentPlayer.getHand().isEmpty()) {
                            showDialogMessage(currentPlayer.getName() + " wins the game!");
                            showWinnerScreen(currentPlayer.getName());
                            return;
                        }

                        moveToNextPlayer();
                        updateDisplay();

                        // If next player is a bot, perform bot move after a short delay
                        if (currentPlayer.getType() == Player.PlayerType.BOT) {
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    performBotMove();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    } else {
                        // Player chose not to play the drawn card, move to next player
                        moveToNextPlayer();
                        updateDisplay();
                        
                        // If next player is a bot, perform bot move after a short delay
                        if (currentPlayer.getType() == Player.PlayerType.BOT) {
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    performBotMove();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                } else {
                    // Drawn card can't be played, move to next player
                    moveToNextPlayer();
                    updateDisplay();
                    
                    // If next player is a bot, perform bot move after a short delay
                    if (currentPlayer.getType() == Player.PlayerType.BOT) {
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                performBotMove();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            } else {
                showDialogMessage("No more cards in the deck!");
            }
        }
    });

    CustomLabel drawLabel = new CustomLabel("Draw Pile");
    drawLabel.setForeground(Color.WHITE);

    CustomPanel drawPanel = new CustomPanel();
    drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
    drawPanel.add(drawPileComponent);
    drawPanel.add(Box.createVerticalStrut(10));
    drawPanel.add(drawLabel);

    panel.add(drawPanel);

    return panel;
}

protected void handleDrawCard() {
	// TODO Auto-generated method stub
	
}



/**
 * Handles playing a card
 */
private void handleCardPlay(Card card) {
    // Only allow human players to play cards
    if (currentPlayer.getType() != Player.PlayerType.HUMAN) {
        return;
    }

    Card topCard = game.getTopCard();

    if (card.canPlayOn(topCard)) {
        // Find card index in player's hand
        int cardIndex = -1;
        for (int i = 0; i < currentPlayer.getHand().size(); i++) {
            if (currentPlayer.getHand().get(i).equals(card)) {
                cardIndex = i;
                break;
            }
        }

        if (cardIndex >= 0) {
            Card playedCard = currentPlayer.playCard(cardIndex);

            // Vérification de l'UNO oublié
            if (currentPlayer.getHand().size() == 1 && !currentPlayer.hasCalledUno()) {
                showFloatingText("UNO oublié ! +2 cartes pour " + currentPlayer.getName());

                // Donner une pénalité : tirer 2 cartes
                for (int i = 0; i < 2; i++) {
                    Card penaltyCard = game.playerDrawsCard(currentPlayer);
                    if (penaltyCard != null) {
                        currentPlayer.addCard(penaltyCard);
                    }
                }
            }

            // Handle wild card color selection
            if (playedCard.getColor() == Card.Color.WILD) {
                showColorPicker(playedCard);
            }

            // Play the card
            game.playCard(playedCard);

            // Show effect text for special cards
            if (isSpecialCard(playedCard)) {
                showFloatingText(currentPlayer.getName() + " played " + playedCard.getValue() + "!");
            }

            // Check for win condition
            if (currentPlayer.hasWon()) {
                showDialogMessage(currentPlayer.getName() + " wins the game!");
                showWinnerScreen(currentPlayer.getName());
                return;
            }

            // Move to next player after processing any special card effects
            moveToNextPlayer();
            updateDisplay();

            // If next player is a bot, perform bot move after a short delay
            if (currentPlayer.getType() == Player.PlayerType.BOT) {
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        performBotMove();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    } else {
        showDialogMessage("Invalid move! This card cannot be played on the current top card.");
    }
}




/**

* Initializes all player panels based on player count

*/

private void initPlayerPanels() {

int playerCount = players.size();

playerPanels = new CustomPanel[playerCount];

playerNameLabels = new CustomLabel[playerCount];


// Always place the human player (first player) at the south position

southPanel = createPlayerPanel(0);

add(southPanel, BorderLayout.SOUTH);


// Position other players based on the total number of players

if (playerCount >= 2) {

northPanel = createPlayerPanel(1);

add(northPanel, BorderLayout.NORTH);

}


if (playerCount >= 3) {

eastPanel = createPlayerPanel(2);

add(eastPanel, BorderLayout.EAST);

}


if (playerCount >= 4) {

westPanel = createPlayerPanel(3);

add(westPanel, BorderLayout.WEST);

}

}


/**

* Creates a panel for displaying a player's hand

*/

private CustomPanel createPlayerPanel(int playerIndex) {

Player player = players.get(playerIndex);


// Create panel with BorderLayout

CustomPanel panel = new CustomPanel(new BorderLayout());

panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


// Create name label

CustomLabel nameLabel = new CustomLabel(player.getName() + " (" + player.getType() + ")");

nameLabel.setForeground(Color.WHITE);

nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

panel.add(nameLabel, BorderLayout.NORTH);

playerNameLabels[playerIndex] = nameLabel;


// Create hand panel with cards

CustomPanel cardsPanel = new CustomPanel();


// Use appropriate layout based on position

if (playerIndex == 0 || playerIndex == 1) { // South or North

cardsPanel.setLayout(new PileLayout());

} else { // East or West

cardsPanel.setLayout(new VerticalPileLayout());

}


// Add cards to the panel

for (Card card : player.getHand()) {

CardComponent cardComp;

// Show cards only for the human player (index 0)

if (playerIndex == 0 || player.getType() == Player.PlayerType.HUMAN) {

cardComp = new CardComponent(card);

// Add click listeners only to the human player's cards

cardComp.addMouseListener(createCardClickListener(card));

} else {

cardComp = new CardComponent(null, true); // Card back for opponents

}


// Set appropriate size based on position

if (playerIndex == 0 || playerIndex == 1) { // South or North

cardComp.setPreferredSize(new Dimension(100, 150));

} else { // East or West

cardComp.setPreferredSize(new Dimension(80, 120));

}


cardsPanel.add(cardComp);

}


// Add scrolling for the cards

ScrollPaneWrapper scrollPane = new ScrollPaneWrapper(cardsPanel);

scrollPane.setHorizontalScrollBarPolicy(ScrollPaneWrapper.HORIZONTAL_SCROLLBAR_AS_NEEDED);

scrollPane.setVerticalScrollBarPolicy(ScrollPaneWrapper.VERTICAL_SCROLLBAR_NEVER);

scrollPane.setBorder(null);


panel.add(scrollPane, BorderLayout.CENTER);

playerPanels[playerIndex] = panel;


return panel;

}


/**

* Updates all display elements based on current game state

*/

public void updateDisplay() {

updateCurrentPlayerInfo();

updateDiscardPile();

updatePlayerPanels();

}


/**

* Updates the current player information

*/

private void updateCurrentPlayerInfo() {

currentPlayer = players.get(currentPlayerIndex);

currentPlayerLabel.setText(currentPlayer.getName() + "'s Turn");


// Update player icon

String iconPath = currentPlayer.getType() == Player.PlayerType.HUMAN ?

"C:/Users/Moi/Pictures/human.jpg" : "C:/Users/Moi/Pictures/robot.jpg";

try {

ImageIcon icon = new ImageIcon(iconPath);

Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

currentPlayerIcon.setIcon(new ImageIcon(scaledIcon));

} catch (Exception e) {

System.err.println("Error loading icon: " + e.getMessage());

}

}


/**

* Updates the discard pile display

*/

private void updateDiscardPile() {

Card topCard = game.getTopCard();

if (topCard != null) {

discardPileComponent.setCard(topCard);

}

}


/**

* Updates all player panels

*/

private void updatePlayerPanels() {

for (int i = 0; i < players.size(); i++) {

Player player = players.get(i);


// Update player name label with card count

playerNameLabels[i].setText(player.getName() + " (" + player.getHand().size() + " cards)");


// Get the cards panel

CustomPanel panel = playerPanels[i];

Component[] components = panel.getComponents();

ScrollPaneWrapper scrollPane = null;


// Find the scroll pane component

for (Component comp : components) {

if (comp instanceof ScrollPaneWrapper) {

scrollPane = (ScrollPaneWrapper) comp;

break;

}

}


if (scrollPane != null) {

// Get the viewport component (cards panel)

Component viewComponent = scrollPane.getViewport().getView();

if (viewComponent instanceof CustomPanel) {

CustomPanel cardsPanel = (CustomPanel) viewComponent;

cardsPanel.removeAll();


// Add updated cards

for (Card card : player.getHand()) {

CardComponent cardComp;

// Show cards only for the current player or if it's a human player

if (i == 0 || player.getType() == Player.PlayerType.HUMAN) {

cardComp = new CardComponent(card);

// Add click listeners only to the current player's cards

if (i == currentPlayerIndex) {

cardComp.addMouseListener(createCardClickListener(card));

}

} else {

cardComp = new CardComponent(null, true); // Card back for opponents

}


// Set appropriate size based on position

if (i == 0 || i == 1) { // South or North

cardComp.setPreferredSize(new Dimension(100, 150));

} else { // East or West

cardComp.setPreferredSize(new Dimension(80, 120));

}


cardsPanel.add(cardComp);

}


cardsPanel.revalidate();

cardsPanel.repaint();

}

}

}

}


/**

* Creates a mouse listener for handling card clicks

*/

private MouseListener createCardClickListener(Card card) {

return new MouseAdapter() {

@Override

public void mouseClicked(MouseEvent e) {

handleCardPlay(card);

}

};

}


/**

* Handles playing a card

*/



/**

* Shows color picker dialog for wild cards

*/

private void showColorPicker(Card wildCard) {

String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};

Object chosen = JOptionPane.showInputDialog(

this,

"Choose a color:",

"Wild Card Color Selection",

JOptionPane.QUESTION_MESSAGE,

null,

colors,

colors[0]

);


if (chosen != null) {

wildCard.setColor(Card.Color.valueOf((String)chosen));

} else {

//Default to RED if dialog is canceled

wildCard.setColor(Card.Color.RED);

}

}


/**

* Shows floating text for special card effects

*/

private void showFloatingText(String text) {

JDialog dialog = new JDialog();

dialog.setUndecorated(true);

dialog.setBackground(new Color(0, 0, 0, 0));

dialog.setAlwaysOnTop(true);


JLabel label = new JLabel(text);

label.setFont(new Font("Arial", Font.BOLD, 24));

label.setForeground(Color.WHITE);

label.setBackground(new Color(0, 0, 0, 150));

label.setOpaque(true);

label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


dialog.add(label);

dialog.pack();


//Center the dialog on the screen

Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

dialog.setLocation(

(screenSize.width - dialog.getWidth()) / 2,

(screenSize.height - dialog.getHeight()) / 2

);


dialog.setVisible(true);


//Hide the dialog after a delay

Timer timer = new Timer(2000, new ActionListener() {

@Override

public void actionPerformed(ActionEvent e) {

dialog.dispose();

}

});

timer.setRepeats(false);

timer.start();

}


/**

* Displays a dialog message

*/

private void showDialogMessage(String message) {

JOptionPane.showMessageDialog(this, message);

}


/**

* Handles UNO button click

*/

public void handleUnoButtonClick() {

if (unoButtonEnabled && currentPlayer.getHand().size() == 1) {

showFloatingText(currentPlayer.getName() + " says UNO!");


//Mark that the player has called "UNO"

currentPlayer.setHasCalledUno(true);


//Disable the UNO button for 2 seconds

unoButtonEnabled = false;

unoButton.setEnabled(false);


Timer timer = new Timer(2000, new ActionListener() {

@Override

public void actionPerformed(ActionEvent e) {

unoButtonEnabled = true;

unoButton.setEnabled(true);

}

});

timer.setRepeats(false);

timer.start();

}

}


/**

* Shows the winner screen

*/

public void showWinnerScreen(String winnerName) {
    // Get the current window frame (the game board view)
    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    // Close the current window (game board view)
    if (currentFrame != null) {
        currentFrame.dispose();
    }

    // Create and show the winner view, passing the winner's name
    WinnerView winnerView = new WinnerView(winnerName);
    winnerView.setVisible(true);


// This is how the win condition should be checked in methods like handleCardPlay:
if (currentPlayer.hasWon()) {
    showDialogMessage(currentPlayer.getName() + " wins the game!");
    showWinnerScreen(currentPlayer.getName());
    return;
}
}
/**

* Moves to the next player in sequence

*/

public void moveToNextPlayer() {

int direction = game.isClockwise() ? 1 : -1;

currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();

currentPlayer = players.get(currentPlayerIndex);


//Reset hasCalledUno for the new player

currentPlayer.setHasCalledUno(false);

}


/**

* Checks if a card is a special action card

*/

public boolean isSpecialCard(Card card) {

return card.getValue() == Card.Value.SKIP ||

card.getValue() == Card.Value.REVERSE ||

card.getValue() == Card.Value.DRAW_TWO ||

card.getValue() == Card.Value.WILD_DRAW_FOUR;

}




/**

* Performs a move for a bot player

*/

private void performBotMove() {
    if (currentPlayer.getType() != Player.PlayerType.BOT) return;

    Card topCard = game.getTopCard();
    List<Card> hand = currentPlayer.getHand();
    boolean[] played = { false }; // Moved outside the timer so it's accessible for the entire method

    showFloatingText(currentPlayer.getName() + " is thinking...");

    // Simulate a delay for the "thinking" effect
    Timer thinkingTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                if (card.canPlayOn(topCard)) {
                    Card playedCard = currentPlayer.playCard(i);

                    if (playedCard.getColor() == Card.Color.WILD) {
                        int[] colorCounts = new int[4]; // RED, GREEN, BLUE, YELLOW
                        for (Card c : hand) {
                            if (c.getColor() != Card.Color.WILD) {
                                colorCounts[c.getColor().ordinal()]++;
                            }
                        }

                        int maxIndex = 0;
                        for (int j = 1; j < colorCounts.length; j++) {
                            if (colorCounts[j] > colorCounts[maxIndex]) {
                                maxIndex = j;
                            }
                        }

                        Card.Color chosenColor = Card.Color.values()[maxIndex];
                        if (chosenColor == Card.Color.WILD) {
                            chosenColor = Card.Color.RED;
                        }

                        playedCard.setColor(chosenColor);
                        showFloatingText(currentPlayer.getName() + " chose " + chosenColor);
                    }

                    game.playCard(playedCard);

                    // Handle UNO
                    if (currentPlayer.getHand().size() == 1) {
                        if (Math.random() < 0.85) {
                            showFloatingText(currentPlayer.getName() + " says UNO!");
                            currentPlayer.setHasCalledUno(true);
                        } else {
                            showFloatingText(currentPlayer.getName() + " forgot to say UNO!");
                            for (int j = 0; j < 2; j++) {
                                Card penaltyCard = game.playerDrawsCard(currentPlayer);
                                if (penaltyCard != null) {
                                    currentPlayer.addCard(penaltyCard);
                                }
                            }
                        }
                    }

                    if (isSpecialCard(playedCard)) {
                        showFloatingText(currentPlayer.getName() + " played " + playedCard.getValue() + "!");
                    } else {
                        showFloatingText(currentPlayer.getName() + " played " + playedCard);
                    }

                    if (currentPlayer.hasWon()) {
                        showDialogMessage(currentPlayer.getName() + " wins the game!");
                        showWinnerScreen(currentPlayer.getName());
                        return;
                    }

                    played[0] = true; // Mark that a card was played
                    break; // Exit loop once a card is played
                }
            }

            if (!played[0]) {
                showFloatingText(currentPlayer.getName() + " draws a card");

                Card drawnCard = game.playerDrawsCard(currentPlayer);
                if (drawnCard != null) {
                    currentPlayer.addCard(drawnCard);

                    if (drawnCard.canPlayOn(topCard)) {
                        currentPlayer.getHand().remove(drawnCard);

                        if (drawnCard.getColor() == Card.Color.WILD) {
                            int[] colorCounts = new int[4];
                            for (Card c : currentPlayer.getHand()) {
                                if (c.getColor() != Card.Color.WILD) {
                                    colorCounts[c.getColor().ordinal()]++;
                                }
                            }

                            int maxIndex = 0;
                            for (int j = 1; j < colorCounts.length; j++) {
                                if (colorCounts[j] > colorCounts[maxIndex]) {
                                    maxIndex = j;
                                }
                            }

                            Card.Color chosenColor = Card.Color.values()[maxIndex];
                            if (chosenColor == Card.Color.WILD) {
                                chosenColor = Card.Color.RED;
                            }

                            drawnCard.setColor(chosenColor);
                            showFloatingText(currentPlayer.getName() + " chose " + chosenColor);
                        }

                        game.playCard(drawnCard);

                        if (currentPlayer.getHand().size() == 1) {
                            if (Math.random() < 0.85) {
                                showFloatingText(currentPlayer.getName() + " says UNO!");
                                currentPlayer.setHasCalledUno(true);
                            } else {
                                showFloatingText(currentPlayer.getName() + " forgot to say UNO!");
                                for (int j = 0; j < 2; j++) {
                                    Card penaltyCard = game.playerDrawsCard(currentPlayer);
                                    if (penaltyCard != null) {
                                        currentPlayer.addCard(penaltyCard);
                                    }
                                }
                            }
                        }

                        if (isSpecialCard(drawnCard)) {
                            showFloatingText(currentPlayer.getName() + " played " + drawnCard.getValue() + "!");
                        } else {
                            showFloatingText(currentPlayer.getName() + " played " + drawnCard);
                        }

                        if (currentPlayer.hasWon()) {
                            showDialogMessage(currentPlayer.getName() + " wins the game!");
                            showWinnerScreen(currentPlayer.getName());
                            return;
                        }
                    }
                }
            }

            moveToNextPlayer();
            updateDisplay();

            if (currentPlayer.getType() == Player.PlayerType.BOT) {
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        performBotMove(); // Call performBotMove recursively for the next bot move
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    });

    thinkingTimer.setRepeats(false);
    thinkingTimer.start();
}
}


