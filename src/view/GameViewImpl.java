package view;
import javax.swing.*;

import controller.GameController;
import unoGamepackage.Bot;
import unoGamepackage.Card;
import unoGamepackage.Color;
import unoGamepackage.Player;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*; // Keep this for Swing components
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import unoGamepackage.Color; // Import the Color enum from unoGamepackage
public class GameViewImpl implements GameView {
    private JFrame frame;
    private JPanel playerHandPanel;
    private JPanel topCardPanel;
    private JLabel gameStatusLabel;
    private JButton playButton;
    private JButton passButton;
    private JLabel messageLabel;
    private GameController controller;

    public GameViewImpl() {
        // Créer la fenêtre principale
        frame = new JFrame("UNO Game");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer le panneau pour afficher les cartes du joueur
        playerHandPanel = new JPanel();
        playerHandPanel.setLayout(new FlowLayout());

        // Créer le panneau pour afficher la carte du dessus
        topCardPanel = new JPanel();
        topCardPanel.setLayout(new FlowLayout());
        
        // Créer un label pour afficher l'état du jeu
        gameStatusLabel = new JLabel("Bienvenue dans le jeu UNO");
        
        // Créer un label pour afficher des messages
        messageLabel = new JLabel("");

        // Créer les boutons pour jouer et passer son tour
        playButton = new JButton("Jouer");
        passButton = new JButton("Passer le tour");

        // Désactiver les boutons par défaut
        playButton.setEnabled(false);
        passButton.setEnabled(true);

        // Add action listeners to buttons
        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPassTurnClicked();
            }
        });

        // Ajouter les composants à la fenêtre principale
        frame.add(gameStatusLabel, BorderLayout.NORTH);
        frame.add(playerHandPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(topCardPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(playButton);
        buttonPanel.add(passButton);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);
        
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    @Override
    public void updatePlayerHand(List<Card> hand) {
        // Vider le panneau avant d'ajouter les nouvelles cartes
        playerHandPanel.removeAll();

        // Ajouter les cartes à l'interface utilisateur
        for (Card card : hand) {
            JButton cardButton = new JButton(card.toString());  // Affiche la carte sous forme de texte
            cardButton.addActionListener(e -> onCardClicked(card));  // Ajouter un écouteur pour le clic
            playerHandPanel.add(cardButton);
        }
        // Rafraîchir l'interface pour afficher les nouvelles cartes
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    @Override
    public void updateTopCard(Card card) {
        // Vider le panneau de la carte du dessus
        topCardPanel.removeAll();
        
        // Ajouter la carte du dessus
        JLabel topCardLabel = new JLabel("Carte du dessus: " + card);
        topCardPanel.add(topCardLabel);
        
        // Rafraîchir l'interface pour afficher la carte du dessus
        topCardPanel.revalidate();
        topCardPanel.repaint();
    }

    @Override
    public void displayMessage(String message) {
        messageLabel.setText(message);  // Afficher un message en bas de l'interface
    }

    @Override
    public void enablePlayButton() {
        playButton.setEnabled(true);  // Activer le bouton "Jouer"
    }

    @Override
    public void disablePlayButton() {
        playButton.setEnabled(false);  // Désactiver le bouton "Jouer"
    }

    @Override
    public void enablePassButton() {
        passButton.setEnabled(true);  // Activer le bouton "Passer son tour"
    }

    @Override
    public void disablePassButton() {
        passButton.setEnabled(false);  // Désactiver le bouton "Passer son tour"
    }

    @Override
    public void updateGameStatus(String status) {
        gameStatusLabel.setText(status);  // Mettre à jour l'état du jeu dans le label
    }

    @Override
    public void setPlayers(List<String> players) {
        // Mise à jour de l'affichage des joueurs (peut être utilisé dans une liste ou un tableau)
        String playersText = "Joueurs: " + String.join(", ", players);
        gameStatusLabel.setText(playersText);  // Afficher la liste des joueurs
    }

    @Override
    public void onCardClicked(Card card) {
        // Gérer l'événement de clic sur une carte
        System.out.println("Carte sélectionnée: " + card);
        // Appeler la méthode du contrôleur pour jouer la carte
        if (controller != null) {
            controller.playCard(card);
        }
    }

    @Override
    public void onPassTurnClicked() {
        // Gérer l'événement de clic sur "Passer son tour"
        System.out.println("Tour passé");
        // Appeler la méthode du contrôleur pour passer le tour
        if (controller != null) {
            controller.passTurn();
        }
    }

    @Override
    public void setController(GameController controller) {
        this.controller = controller;
    }
    
    // New methods required by the GameController
    
    @Override
    public void updateGameState(List<Player> players, int currentPlayerIndex, Card topCard, int direction, Player winner) {
        // Update the game state display
        updateTopCard(topCard);
        
        // Update status label with current player
        String dirText = direction > 0 ? "horaire" : "anti-horaire";
        String statusText = "Tour de " + players.get(currentPlayerIndex).getName() + 
                            " (Direction: " + dirText + ")";
        
        if (winner != null) {
            statusText = "Partie terminée! " + winner.getName() + " a gagné!";
        }
        
        updateGameStatus(statusText);
        
        // Update current player's hand if it's a human player
        Player currentPlayer = players.get(currentPlayerIndex);
        if (!(currentPlayer instanceof Bot)) {
            updatePlayerHand(currentPlayer.getHand());
        }
    }
    
    @Override
    public void showCardPlayed(Player player, Card card) {
        displayMessage(player.getName() + " a joué " + card.toString());
    }
    
    @Override
    public void showInvalidMove(Player player, Card card) {
        displayMessage("Mouvement invalide! " + card.toString() + " ne peut pas être joué.");
    }
    
    @Override
    public void showCardDrawn(Player player, Card card) {
        displayMessage(player.getName() + " a pioché une carte.");
    }
    
    @Override
    public void showPlayerSkipped(Player player) {
        displayMessage(player.getName() + " a été sauté!");
    }
    
    @Override
    public void showDirectionChanged(boolean isReversed) {
        String direction = isReversed ? "anti-horaire" : "horaire";
        displayMessage("La direction a changé! Maintenant " + direction);
    }
    
    @Override
    public void showGameResult(Player winner) {
        JOptionPane.showMessageDialog(frame, 
                                     winner.getName() + " a gagné la partie!", 
                                     "Fin de partie", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void promptColorSelection() {
        // Create color selection dialog
        JDialog colorDialog = new JDialog(frame, "Sélectionnez une couleur", true);
        colorDialog.setLayout(new GridLayout(2, 2));
        
        // Create color buttons
        JButton redButton = new JButton("Rouge");
        redButton.setBackground(java.awt.Color.RED);
        redButton.addActionListener(e -> {
            if (controller != null) {
                controller.selectColor(Color.RED);
            }
            colorDialog.dispose();
        });
        
        JButton blueButton = new JButton("Bleu");
        blueButton.setBackground(java.awt.Color.BLUE);
        blueButton.setForeground(java.awt.Color.WHITE);
        blueButton.addActionListener(e -> {
            if (controller != null) {
                controller.selectColor(Color.BLUE);
            }
            colorDialog.dispose();
        });
        
        JButton greenButton = new JButton("Vert");
        greenButton.setBackground(java.awt.Color.GREEN);
        greenButton.addActionListener(e -> {
            if (controller != null) {
                controller.selectColor(Color.GREEN);
            }
            colorDialog.dispose();
        });
        
        JButton yellowButton = new JButton("Jaune");
        yellowButton.setBackground(java.awt.Color.YELLOW);
        yellowButton.addActionListener(e -> {
            if (controller != null) {
                controller.selectColor(unoGamepackage.Color.YELLOW);
            }
            colorDialog.dispose();
        });
        
        // Add buttons to dialog
        colorDialog.add(redButton);
        colorDialog.add(blueButton);
        colorDialog.add(greenButton);
        colorDialog.add(yellowButton);
        
        // Show dialog
        colorDialog.setSize(300, 200);
        colorDialog.setLocationRelativeTo(frame);
        colorDialog.setVisible(true);
    }
    
    @Override
    public void showColorChanged(Color color) {
        String colorName = "Inconnue";
        if (color == Color.RED) colorName = "Rouge";
        else if (color == Color.BLUE) colorName = "Bleu";
        else if (color == Color.GREEN) colorName = "Vert";
        else if (color == Color.YELLOW) colorName = "Jaune";
        else if (color == Color.WILD) colorName = "Joker";
        
        displayMessage("La couleur a été changée à " + colorName);
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getPassButton() {
        return passButton;
    }
}