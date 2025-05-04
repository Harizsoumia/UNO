package viewunogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.CustomPanel;
import view.CustomButton;
import view.CustomLabel;
import view.CustomComboBox;
import view.CustomFrame;
import view.CustomTextField;
import view.CustomImageLabel;
import model.UnoGame;
import model.Player;

/**
 * Vue d'accueil du jeu UNO
 */
public class AccueilView extends CustomFrame {
    
    // Composants de l'interface
    private CustomPanel mainPanel;
    private CustomImageLabel unoImageLabel;
    private CustomLabel titleLabel;
    private CustomLabel playerCountLabel;
    private CustomComboBox<String> playerCountComboBox;
    private CustomLabel[] playerLabels = new CustomLabel[4];
    private CustomTextField[] playerNameFields = new CustomTextField[4];
    private CustomLabel[] playerTypeLabels = new CustomLabel[4];
    private CustomComboBox<String>[] playerTypeComboBoxes = new CustomComboBox[4];
    private CustomImageLabel[] playerIconLabels = new CustomImageLabel[4];
    private CustomPanel[] playerPanels = new CustomPanel[4];
    private CustomButton startGameButton;
    
    /**
     * Constructeur
     */
    public AccueilView() {
        // Configuration de la fenêtre
        super("UNO Game");
        applyTheme("uno"); // Appliquer le thème UNO
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialisation des composants
        initComponents();
        
        // Affichage de la fenêtre
        setVisible(true);
    }
    
    /**
     * Initialise les composants de l'interface
     */
    private void initComponents() {
        // Création du panel principal
        mainPanel = new CustomPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel du haut pour le titre et l'image
        CustomPanel topPanel = new CustomPanel(new BorderLayout(20, 0));
        
        // Ajout de l'image UNO
        unoImageLabel = new CustomImageLabel();
        try {
            ImageIcon unoImage = new ImageIcon("C:\\Users\\PC\\Downloads\\unogameyousra.jpg");
            Image scaledImage = unoImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            unoImageLabel.setIcon(new ImageIcon(scaledImage));
            topPanel.add(unoImageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image UNO: " + e.getMessage());
        }
        
        // Titre du jeu
        titleLabel = new CustomLabel("UNO GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 90));
        titleLabel.setForeground(Color.BLACK);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Ajout du panel du haut au panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel du centre pour la configuration des joueurs
        CustomPanel centerPanel = new CustomPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Sélection du nombre de joueurs
        CustomPanel playerCountPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        playerCountLabel = new CustomLabel("Nombre de joueurs: ");
        playerCountLabel.setForeground(Color.BLACK);
        playerCountPanel.add(playerCountLabel);
        
        String[] playerCounts = {"2", "3", "4"};
        playerCountComboBox = new CustomComboBox<>(playerCounts);
        playerCountComboBox.setSelectedIndex(0);
        playerCountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlayerPanels();
            }
        });
        playerCountPanel.add(playerCountComboBox);
        
        centerPanel.add(playerCountPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Panel pour la configuration des joueurs
        CustomPanel playersPanel = new CustomPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        
        // Initialisation des panels de configuration des joueurs
        for (int i = 0; i < 4; i++) {
            final int playerIndex = i; // Pour utilisation dans le ActionListener
            
            playerPanels[i] = new CustomPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            
            // Label du joueur
            playerLabels[i] = new CustomLabel("Joueur " + (i + 1) + ": ");
            playerLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerLabels[i]);
            
            // Champ de texte pour le nom du joueur
            playerNameFields[i] = new CustomTextField(" ", 10);
            playerPanels[i].add(playerNameFields[i]);
            
            // Label et ComboBox pour le type de joueur
            playerTypeLabels[i] = new CustomLabel("Type: ");
            playerTypeLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerTypeLabels[i]);
            
            String[] playerTypes = {"Humain", "Robot"};
            playerTypeComboBoxes[i] = new CustomComboBox<>(playerTypes);
            playerTypeComboBoxes[i].setSelectedIndex(i == 0 ? 0 : 1); // Premier joueur toujours humain
            
            // Le joueur 1 est toujours humain (non modifiable)
            if (i == 0) {
                playerTypeComboBoxes[i].setEnabled(false);
            }
            
            // Ajouter un écouteur pour mettre à jour l'icône
            playerTypeComboBoxes[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updatePlayerIcon(playerIndex);
                }
            });
            
            playerPanels[i].add(playerTypeComboBoxes[i]);
            
            // Icône du joueur
            playerIconLabels[i] = new CustomImageLabel();
            updatePlayerIcon(i);
            playerPanels[i].add(playerIconLabels[i]);
            
            // Ajouter le panel du joueur
            playersPanel.add(playerPanels[i]);
            playersPanel.add(Box.createVerticalStrut(10));
        }
        
        // Mise à jour des panels de joueurs
        updatePlayerPanels();
        
        centerPanel.add(playersPanel);
        
        // Ajout du panel central au panel principal
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel du bas pour le bouton de démarrage
        CustomPanel bottomPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        startGameButton = new CustomButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        bottomPanel.add(startGameButton);
        
        // Ajout du panel du bas au panel principal
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    /**
     * Met à jour l'affichage des panels des joueurs selon le nombre sélectionné
     */
    private void updatePlayerPanels() {
        int playerCount = Integer.parseInt((String) playerCountComboBox.getSelectedItem());
        
        // Afficher ou masquer les panels selon le nombre de joueurs
        for (int i = 0; i < playerPanels.length; i++) {
            playerPanels[i].setVisible(i < playerCount);
        }
        
        // Rafraîchir l'affichage
        revalidate();
        repaint();
    }
    
    /**
     * Met à jour l'icône du joueur en fonction du type sélectionné
     */
    private void updatePlayerIcon(int playerIndex) {
        String playerType = (String) playerTypeComboBoxes[playerIndex].getSelectedItem();
        String iconPath = playerType.equals("Humain") ? 
            "C:\\Users\\PC\\Downloads\\logo human.jpg" : 
            "C:\\Users\\PC\\Downloads\\logobot.jpg";
        
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            playerIconLabels[playerIndex].setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône: " + e.getMessage());
            playerIconLabels[playerIndex].setIcon(null);
        }
    }
    
    /**
     * Démarre le jeu avec les paramètres sélectionnés
     */
    private void startGame() {
        // Get the selected number of players from the ComboBox
        int playerCount = Integer.parseInt((String) playerCountComboBox.getSelectedItem());
        ArrayList<Player> players = new ArrayList<>();

        // Collect player data from the UI (player names and types)
        for (int i = 0; i < playerCount; i++) {
            // Get the player's name from the corresponding text field
            String name = playerNameFields[i].getText().trim();

            // Use a default name if the field is empty
            if (name.isEmpty()) {
                name = "Joueur " + (i + 1);
            }

            // Get the player's type (Human or Bot) from the corresponding ComboBox
            String typeString = (String) playerTypeComboBoxes[i].getSelectedItem();
            Player.PlayerType type = typeString.equals("Humain") ? Player.PlayerType.HUMAN : Player.PlayerType.BOT;

            // Add the player to the list
            players.add(new Player(name, type));
        }

        // Initialize the UnoGame with the created players
        UnoGame unoGame = new UnoGame();
        unoGame.initializeGame(players);

        // Dispose of the current view (the start game page)
        dispose();

        // Show a confirmation message before transitioning to the game view
        JOptionPane.showMessageDialog(null, 
            "Le jeu va commencer avec " + playerCount + " joueurs !", 
            "UNO Game", 
            JOptionPane.INFORMATION_MESSAGE);

        // Create a new JFrame to hold the game board
        JFrame gameFrame = new JFrame("UNO Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(1000, 800);
        gameFrame.setLocationRelativeTo(null);
        
        // Create and add the GameBoardPage to the frame
        GameBoardPage gameBoardView = new GameBoardPage(unoGame);
        gameFrame.add(gameBoardView);
        gameFrame.setVisible(true);
    }

}package viewunogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.CustomPanel;
import view.CustomButton;
import view.CustomLabel;
import view.CustomComboBox;
import view.CustomFrame;
import view.CustomTextField;
import view.CustomImageLabel;
import model.UnoGame;
import model.Player;

/**
 * Vue d'accueil du jeu UNO
 */
public class AccueilView extends CustomFrame {
    
    // Composants de l'interface
    private CustomPanel mainPanel;
    private CustomImageLabel unoImageLabel;
    private CustomLabel titleLabel;
    private CustomLabel playerCountLabel;
    private CustomComboBox<String> playerCountComboBox;
    private CustomLabel[] playerLabels = new CustomLabel[4];
    private CustomTextField[] playerNameFields = new CustomTextField[4];
    private CustomLabel[] playerTypeLabels = new CustomLabel[4];
    private CustomComboBox<String>[] playerTypeComboBoxes = new CustomComboBox[4];
    private CustomImageLabel[] playerIconLabels = new CustomImageLabel[4];
    private CustomPanel[] playerPanels = new CustomPanel[4];
    private CustomButton startGameButton;
    
    /**
     * Constructeur
     */
    public AccueilView() {
        // Configuration de la fenêtre
        super("UNO Game");
        applyTheme("uno"); // Appliquer le thème UNO
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialisation des composants
        initComponents();
        
        // Affichage de la fenêtre
        setVisible(true);
    }
    
    /**
     * Initialise les composants de l'interface
     */
    private void initComponents() {
        // Création du panel principal
        mainPanel = new CustomPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel du haut pour le titre et l'image
        CustomPanel topPanel = new CustomPanel(new BorderLayout(20, 0));
        
        // Ajout de l'image UNO
        unoImageLabel = new CustomImageLabel();
        try {
            ImageIcon unoImage = new ImageIcon("C:\\Users\\PC\\Downloads\\unogameyousra.jpg");
            Image scaledImage = unoImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            unoImageLabel.setIcon(new ImageIcon(scaledImage));
            topPanel.add(unoImageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image UNO: " + e.getMessage());
        }
        
        // Titre du jeu
        titleLabel = new CustomLabel("UNO GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 90));
        titleLabel.setForeground(Color.BLACK);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Ajout du panel du haut au panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel du centre pour la configuration des joueurs
        CustomPanel centerPanel = new CustomPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Sélection du nombre de joueurs
        CustomPanel playerCountPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        playerCountLabel = new CustomLabel("Nombre de joueurs: ");
        playerCountLabel.setForeground(Color.BLACK);
        playerCountPanel.add(playerCountLabel);
        
        String[] playerCounts = {"2", "3", "4"};
        playerCountComboBox = new CustomComboBox<>(playerCounts);
        playerCountComboBox.setSelectedIndex(0);
        playerCountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlayerPanels();
            }
        });
        playerCountPanel.add(playerCountComboBox);
        
        centerPanel.add(playerCountPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Panel pour la configuration des joueurs
        CustomPanel playersPanel = new CustomPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        
        // Initialisation des panels de configuration des joueurs
        for (int i = 0; i < 4; i++) {
            final int playerIndex = i; // Pour utilisation dans le ActionListener
            
            playerPanels[i] = new CustomPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            
            // Label du joueur
            playerLabels[i] = new CustomLabel("Joueur " + (i + 1) + ": ");
            playerLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerLabels[i]);
            
            // Champ de texte pour le nom du joueur
            playerNameFields[i] = new CustomTextField(" ", 10);
            playerPanels[i].add(playerNameFields[i]);
            
            // Label et ComboBox pour le type de joueur
            playerTypeLabels[i] = new CustomLabel("Type: ");
            playerTypeLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerTypeLabels[i]);
            
            String[] playerTypes = {"Humain", "Robot"};
            playerTypeComboBoxes[i] = new CustomComboBox<>(playerTypes);
            playerTypeComboBoxes[i].setSelectedIndex(i == 0 ? 0 : 1); // Premier joueur toujours humain
            
            // Le joueur 1 est toujours humain (non modifiable)
            if (i == 0) {
                playerTypeComboBoxes[i].setEnabled(false);
            }
            
            // Ajouter un écouteur pour mettre à jour l'icône
            playerTypeComboBoxes[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updatePlayerIcon(playerIndex);
                }
            });
            
            playerPanels[i].add(playerTypeComboBoxes[i]);
            
            // Icône du joueur
            playerIconLabels[i] = new CustomImageLabel();
            updatePlayerIcon(i);
            playerPanels[i].add(playerIconLabels[i]);
            
            // Ajouter le panel du joueur
            playersPanel.add(playerPanels[i]);
            playersPanel.add(Box.createVerticalStrut(10));
        }
        
        // Mise à jour des panels de joueurs
        updatePlayerPanels();
        
        centerPanel.add(playersPanel);
        
        // Ajout du panel central au panel principal
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel du bas pour le bouton de démarrage
        CustomPanel bottomPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        startGameButton = new CustomButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        bottomPanel.add(startGameButton);
        
        // Ajout du panel du bas au panel principal
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    /**
     * Met à jour l'affichage des panels des joueurs selon le nombre sélectionné
     */
    private void updatePlayerPanels() {
        int playerCount = Integer.parseInt((String) playerCountComboBox.getSelectedItem());
        
        // Afficher ou masquer les panels selon le nombre de joueurs
        for (int i = 0; i < playerPanels.length; i++) {
            playerPanels[i].setVisible(i < playerCount);
        }
        
        // Rafraîchir l'affichage
        revalidate();
        repaint();
    }
    
    /**
     * Met à jour l'icône du joueur en fonction du type sélectionné
     */
    private void updatePlayerIcon(int playerIndex) {
        String playerType = (String) playerTypeComboBoxes[playerIndex].getSelectedItem();
        String iconPath = playerType.equals("Humain") ? 
            "C:\\Users\\PC\\Downloads\\logo human.jpg" : 
            "C:\\Users\\PC\\Downloads\\logobot.jpg";
        
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            playerIconLabels[playerIndex].setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône: " + e.getMessage());
            playerIconLabels[playerIndex].setIcon(null);
        }
    }
    
    /**
     * Démarre le jeu avec les paramètres sélectionnés
     */
    private void startGame() {
        // Get the selected number of players from the ComboBox
        int playerCount = Integer.parseInt((String) playerCountComboBox.getSelectedItem());
        ArrayList<Player> players = new ArrayList<>();

        // Collect player data from the UI (player names and types)
        for (int i = 0; i < playerCount; i++) {
            // Get the player's name from the corresponding text field
            String name = playerNameFields[i].getText().trim();

            // Use a default name if the field is empty
            if (name.isEmpty()) {
                name = "Joueur " + (i + 1);
            }

            // Get the player's type (Human or Bot) from the corresponding ComboBox
            String typeString = (String) playerTypeComboBoxes[i].getSelectedItem();
            Player.PlayerType type = typeString.equals("Humain") ? Player.PlayerType.HUMAN : Player.PlayerType.BOT;

            // Add the player to the list
            players.add(new Player(name, type));
        }

        // Initialize the UnoGame with the created players
        UnoGame unoGame = new UnoGame();
        unoGame.initializeGame(players);

        // Dispose of the current view (the start game page)
        dispose();

        // Show a confirmation message before transitioning to the game view
        JOptionPane.showMessageDialog(null, 
            "Le jeu va commencer avec " + playerCount + " joueurs !", 
            "UNO Game", 
            JOptionPane.INFORMATION_MESSAGE);

        // Create a new JFrame to hold the game board
        JFrame gameFrame = new JFrame("UNO Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(1000, 800);
        gameFrame.setLocationRelativeTo(null);
        
        // Create and add the GameBoardPage to the frame
        GameBoardPage gameBoardView = new GameBoardPage(unoGame);
        gameFrame.add(gameBoardView);
        gameFrame.setVisible(true);
    }

}

