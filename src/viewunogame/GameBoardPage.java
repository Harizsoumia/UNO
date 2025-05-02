package viewunogame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



import model.Card;
import model.Player;
import model.UnoGame;
import view.*;

/**
 * GameBoardPage - Main game interface for the UNO game
 * Displays the game board with player hands, discard pile, and draw pile
 */
public class GameBoardPage extends CustomPanel {
    
    private UnoGame game;
    private ArrayList<Player> players;
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
    private CustomPanel[] opponentPanels;
    private CustomLabel[] opponentLabels;
    private CustomPanel[] opponentCardPanels;
    
    private CustomButton unoButton;
    private boolean unoButtonEnabled = true;
    
    /**
     * Constructor for the game board page
     * @param game The UNO game instance
     * @param players List of players in the game
     */
    public GameBoardPage(UnoGame game, ArrayList<Player> players) {
        super(new BorderLayout(10, 10));
        this.game = game;
        this.players = players;
        this.currentPlayerIndex = 0;
        this.currentPlayer = players.get(currentPlayerIndex);
        
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initComponents();
        updateDisplay();
    }
    
    /**
     * Initialize all UI components
     */
    private void initComponents() {
        // North panel - Current player info
        northPanel = createNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        
        // Center panel - Discard and Draw piles
        centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // South panel - Current player's hand
        southPanel = createSouthPanel();
        add(southPanel, BorderLayout.SOUTH);
        
        // East and West panels - Opponent hands
        eastPanel = createSidePanel(true);
        add(eastPanel, BorderLayout.EAST);
        
        westPanel = createSidePanel(false);
        add(westPanel, BorderLayout.WEST);
    }
    
    /**
     * Creates the north panel with current player info
     */
    private CustomPanel createNorthPanel() {
        CustomPanel panel = new CustomPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        currentPlayerLabel = new CustomLabel();
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        currentPlayerLabel.setForeground(Color.WHITE);
        panel.add(currentPlayerLabel);
        
        currentPlayerIcon = new CustomImageLabel();
        panel.add(currentPlayerIcon);
        
        unoButton = new CustomButton("UNO!");
        unoButton.setFont(new Font("Arial", Font.BOLD, 16));
        unoButton.setPreferredSize(new Dimension(80, 40));
        unoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleUnoButtonClick();
            }
        });
        panel.add(unoButton);
        
        return panel;
    }
    
    /**
     * Creates the center panel with discard and draw piles
     */
    private CustomPanel createCenterPanel() {
        CustomPanel panel = new CustomPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        
        // Discard pile
        Card topCard = game.getTopCard(); // Get top card from game
        discardPileComponent = new CardComponent(topCard);
        discardPileComponent.setPreferredSize(new Dimension(120, 180));
        
        // Label for discard pile
        CustomLabel discardLabel = new CustomLabel("Discard Pile");
        discardLabel.setForeground(Color.WHITE);
        
        CustomPanel discardPanel = new CustomPanel();
        discardPanel.setLayout(new BoxLayout(discardPanel, BoxLayout.Y_AXIS));
        discardPanel.add(discardPileComponent);
        discardPanel.add(Box.createVerticalStrut(10));
        discardPanel.add(discardLabel);
        
        panel.add(discardPanel);
        
        // Draw pile
        drawPileComponent = new CardComponent(null, true); // Using back of card
        drawPileComponent.setPreferredSize(new Dimension(120, 180));
        drawPileComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleDrawCard();
            }
        });
        
        // Label for draw pile
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
    
    /**
     * Creates the south panel with current player's hand
     */
    private CustomPanel createSouthPanel() {
        CustomPanel panel = new CustomPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create hand panel with pile layout
        handPanel = new CustomPanel();
        handPanel.setLayout(new PileLayout());
        
        // Add scroll pane for hand (horizontal scrolling)
        ScrollPaneWrapper scrollPane = new ScrollPaneWrapper(handPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneWrapper.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneWrapper.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Label for player's hand
        CustomLabel handLabel = new CustomLabel("Your Hand");
        handLabel.setForeground(Color.WHITE);
        handLabel.setHorizontalAlignment(CustomLabel.CENTER);
        panel.add(handLabel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Creates a side panel (east or west) for opponent hands
     */
    private CustomPanel createSidePanel(boolean isEast) {
        CustomPanel panel = new CustomPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 400));
        
        // Calculate how many opponents to show on this side
        int totalOpponents = players.size() - 1;
        int opponentsOnThisSide = totalOpponents / 2;
        if (!isEast && totalOpponents % 2 != 0) {
            opponentsOnThisSide++; // Put extra opponent on west side
        }
        
        // Initialize arrays for opponent panels if not already done
        if (opponentPanels == null) {
            opponentPanels = new CustomPanel[totalOpponents];
            opponentLabels = new CustomLabel[totalOpponents];
            opponentCardPanels = new CustomPanel[totalOpponents];
        }
        
        // Create panels for each opponent on this side
        int startIndex = isEast ? 0 : (totalOpponents / 2) + (totalOpponents % 2);
        int endIndex = isEast ? opponentsOnThisSide : totalOpponents;
        
        for (int i = startIndex; i < endIndex; i++) {
            int playerIndex = (currentPlayerIndex + i + 1) % players.size();
            Player opponent = players.get(playerIndex);
            
            CustomPanel opponentPanel = new CustomPanel();
            opponentPanel.setLayout(new BoxLayout(opponentPanel, BoxLayout.Y_AXIS));
            opponentPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            
            // Opponent name and type
            CustomLabel nameLabel = new CustomLabel(opponent.getName());
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            opponentPanel.add(nameLabel);
            opponentPanel.add(Box.createVerticalStrut(5));
            
            // Icon based on player type
            CustomImageLabel iconLabel = new CustomImageLabel();
            String iconPath = opponent.getType() == Player.PlayerType.HUMAN ? 
                "C:\\Users\\PC\\Downloads\\logo human.jpg" : 
                "C:\\Users\\PC\\Downloads\\logobot.jpg";
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaledIcon));
            } catch (Exception e) {
                System.err.println("Error loading icon: " + e.getMessage());
            }
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            opponentPanel.add(iconLabel);
            opponentPanel.add(Box.createVerticalStrut(10));
            
            // Cards display (vertical stack)
            CustomPanel cardsPanel = new CustomPanel();
            cardsPanel.setLayout(new VerticalPileLayout());
            cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            opponentPanel.add(cardsPanel);
            
            // Store references to update later
            opponentPanels[i] = opponentPanel;
            opponentLabels[i] = nameLabel;
            opponentCardPanels[i] = cardsPanel;
            
            panel.add(opponentPanel);
            panel.add(Box.createVerticalGlue());
        }
        
        return panel;
    }
    
    /**
     * Updates all display elements based on current game state
     */
    public void updateDisplay() {
        updateCurrentPlayerInfo();
        updateDiscardPile();
        updateHandDisplay();
        updateOpponentHands();
    }
    
    /**
     * Updates the current player information
     */
    private void updateCurrentPlayerInfo() {
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayerLabel.setText(currentPlayer.getName() + "'s Turn");
        
        // Update player icon
        String iconPath = currentPlayer.getType() == Player.PlayerType.HUMAN ? 
            "C:\\Users\\PC\\Downloads\\logo human.jpg" : 
            "C:\\Users\\PC\\Downloads\\logobot.jpg";
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
     * Updates the current player's hand display
     */
    private void updateHandDisplay() {
        handPanel.removeAll();
        
        for (Card card : currentPlayer.getHand()) {
            CardComponent cardComponent = new CardComponent(card);
            cardComponent.addMouseListener(createCardClickListener(card));
            handPanel.add(cardComponent);
        }
        
        handPanel.revalidate();
        handPanel.repaint();
    }
    
    /**
     * Updates all opponent hand displays
     */
    private void updateOpponentHands() {
        if (opponentCardPanels == null) return;
        
        int opponentCount = 0;
        for (int i = 1; i < players.size(); i++) {
            int playerIndex = (currentPlayerIndex + i) % players.size();
            Player opponent = players.get(playerIndex);
            
            if (opponentCount < opponentCardPanels.length) {
                CustomPanel cardPanel = opponentCardPanels[opponentCount];
                cardPanel.removeAll();
                
                // Add card backs for each card in opponent's hand
                for (int j = 0; j < opponent.getHand().size(); j++) {
                    CardComponent opponentCard = new CardComponent(null, true);
                    opponentCard.setPreferredSize(new Dimension(60, 90));
                    cardPanel.add(opponentCard);
                }
                
                cardPanel.revalidate();
                cardPanel.repaint();
                
                // Update opponent name
                opponentLabels[opponentCount].setText(opponent.getName() + " (" + opponent.getHand().size() + ")");
            }
            
            opponentCount++;
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
    private void handleCardPlay(Card card) {
        Card topCard = game.getTopCard();
        
        if (card.canPlayOn(topCard)) {
            // Find card index in player's hand
            int cardIndex = -1;
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                if (currentPlayer.getHand().get(i) == card) {
                    cardIndex = i;
                    break;
                }
            }
            
            if (cardIndex >= 0) {
                // Handle wild card color selection
                if (card.getColor() == Card.Color.WILD) {
                    showColorPicker(card);
                }
                
                // Play the card
                Card playedCard = currentPlayer.playCard(cardIndex);
                game.playCard(playedCard);
                
                // Show effect text for special cards
                if (isSpecialCard(card)) {
                    showFloatingText(currentPlayer.getName() + " played " + card.getValue() + "!");
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
            }
        } else {
            showDialogMessage("Invalid move! This card cannot be played on the current top card.");
        }
    }
    
    /**
     * Handles drawing a card
     */
    private void handleDrawCard() {
        Card drawnCard = game.drawCardFromDeck();
        if (drawnCard != null) {
            currentPlayer.addCard(drawnCard);
            moveToNextPlayer();
            updateDisplay();
        } else {
            showDialogMessage("No more cards in the deck!");
        }
    }
    
    /**
     * Shows color picker dialog for wild cards
     */
    private void showColorPicker(Card wildCard) {
        String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};
        Object chosen = DialogHelper.showInputDialog(
            this,
            "Choose a color:",
            "Wild Card Color Selection",
            DialogHelper.QUESTION_MESSAGE,
            null,
            colors,
            colors[0]
        );
        
        if (chosen != null) {
            wildCard.setColor(Card.Color.valueOf((String)chosen));
        } else {
            // Default to RED if dialog is canceled
            wildCard.setColor(Card.Color.RED);
        }
    }
    
    /**
     * Shows floating text for special card effects
     */
    private void showFloatingText(String text) {
        FloatingTextDialog dialog = new FloatingTextDialog(text);
        dialog.showFor(2000);
    }
    
    /**
     * Displays a dialog message
     */
    private void showDialogMessage(String message) {
        DialogHelper.showMessageDialog(this, message);
    }
    
    /**
     * Handles UNO button click
     */
    private void handleUnoButtonClick() {
        if (unoButtonEnabled && currentPlayer.getHand().size() == 1) {
            showFloatingText(currentPlayer.getName() + " says UNO!");
            
            // Disable UNO button for 2 seconds
            unoButtonEnabled = false;
            unoButton.setEnabled(false);
            
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    unoButtonEnabled = true;
                    unoButton.setEnabled(true);
                }
            }, 2000);
        }
    }
    
    /**
     * Shows the winner screen
     */
    private void showWinnerScreen(String winnerName) {
        Container frame = SwingUtilities.getWindowAncestor(this);
        if (frame instanceof Window) {
            ((Window)frame).dispose();
        }
        
        new WinnerView(winnerName);
    }
    
    /**
     * Moves to the next player in sequence
     */
    private void moveToNextPlayer() {
        if (game.isClockwise()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        currentPlayer = players.get(currentPlayerIndex);
    }
    
    /**
     * Checks if a card is a special action card
     */
    private boolean isSpecialCard(Card card) {
        return card.getValue() == Card.Value.SKIP || 
               card.getValue() == Card.Value.REVERSE || 
               card.getValue() == Card.Value.DRAW_TWO || 
               card.getValue() == Card.Value.WILD_DRAW_FOUR;
    }
    
    /**
     * Custom layout manager for pile-style card display
     */
    class PileLayout implements LayoutManager {
        private static final int CARD_WIDTH = 100;
        private static final int CARD_HEIGHT = 150;
        private static final int OVERLAP = 20;
        
        @Override
        public void addLayoutComponent(String name, Component comp) {}
        
        @Override
        public void removeLayoutComponent(Component comp) {}
        
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int componentCount = parent.getComponentCount();
            if (componentCount == 0) return new Dimension(0, CARD_HEIGHT);
            
            int width = OVERLAP * (componentCount - 1) + CARD_WIDTH;
            return new Dimension(width, CARD_HEIGHT);
        }
        
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }
        
        @Override
        public void layoutContainer(Container parent) {
            int componentCount = parent.getComponentCount();
            
            for (int i = 0; i < componentCount; i++) {
                Component c = parent.getComponent(i);
                c.setBounds(i * OVERLAP, 0, CARD_WIDTH, CARD_HEIGHT);
            }
        }
    }
    
    /**
     * Custom layout manager for vertical pile-style card display
     */
    class VerticalPileLayout implements LayoutManager {
        private static final int CARD_WIDTH = 60;
        private static final int CARD_HEIGHT = 90;
        private static final int OVERLAP = 15;
        
        @Override
        public void addLayoutComponent(String name, Component comp) {}
        
        @Override
        public void removeLayoutComponent(Component comp) {}
        
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int componentCount = parent.getComponentCount();
            if (componentCount == 0) return new Dimension(CARD_WIDTH, 0);
            
            int height = OVERLAP * (componentCount - 1) + CARD_HEIGHT;
            return new Dimension(CARD_WIDTH, height);
        }
        
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }
        
        @Override
        public void layoutContainer(Container parent) {
            int componentCount = parent.getComponentCount();
            
            for (int i = 0; i < componentCount; i++) {
                Component c = parent.getComponent(i);
                c.setBounds(0, i * OVERLAP, CARD_WIDTH, CARD_HEIGHT);
            }
        }
    }
}