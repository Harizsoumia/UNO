package controller;

import viewunogame.AccueilView;
import viewunogame.GameView;
import unoGamepackage.Game;
import unoGamepackage.Player;

import view.CustomTextField;
import view.CustomComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * Controller class for the UNO game that connects the view and model components
 * following the MVC architecture pattern.
 */
public class UnoGameController {
    
    private AccueilView setupView;
    private Game gameModel;
    private GameView gameView;

    /**
     * Constructor initializes the controller with the setup view
     */
    public UnoGameController() {
        // Initialize the setup view
        this.setupView = new AccueilView();
        
        // Register action listeners
        addSetupViewListeners();
    }

    /**
     * Adds action listeners to the setup view components
     */
    private void addSetupViewListeners() {
        // Add listener to player count combo box
        setupView.getPlayerCountComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupView.refreshPlayerPanels(); // Correct method name
            }
        });
        
        // Add listener to start button
        setupView.getStartGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
    }

    /**
     * Initializes and starts a new game with the selected settings
     */
    private void startGame() {
        ArrayList<CustomTextField> nameFields = setupView.getPlayerNameFields();
        ArrayList<CustomComboBox<String>> typeCombos = setupView.getPlayerTypeComboBoxes();
        
        int playerCount = Integer.parseInt((String) setupView.getPlayerCountComboBox().getSelectedItem());
        
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<Player.PlayerType> playerTypes = new ArrayList<>();
        
        for (int i = 0; i < playerCount; i++) {
            String name = nameFields.get(i).getText().trim();
            if (name.isEmpty()) {
                name = "Joueur " + (i + 1);
            }
            playerNames.add(name);
            
            String type = (String) typeCombos.get(i).getSelectedItem();
            playerTypes.add(type.equals("Humain") ? Player.PlayerType.HUMAN : Player.PlayerType.BOT);
        }
        
        // Initialize the game model
        gameModel = new Game();
        gameModel.initializeGame(playerNames, playerTypes);
        
        // Close the setup view
        setupView.dispose();
        
        // Create and show the GameView
        SwingUtilities.invokeLater(() -> {
            gameView = new GameView(playerNames);
            updateGameView();
            addGameViewListeners();
            startGameLoop();
        });
    }

    /**
     * Updates the game view with the latest game state
     */
    private void updateGameView() {
        // TODO: Implement if needed
    }

    /**
     * Adds listeners for the game view (ex: playing a card, ending turn, etc.)
     */
    private void addGameViewListeners() {
        // TODO: Implement if needed
    }

    /**
     * Starts the game loop, especially for bot players
     */
    private void startGameLoop() {
        // TODO: Implement if needed
    }
}
