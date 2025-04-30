package viewunogame;

import javax.swing.*;
import java.awt.*;
import view.CustomPanel;
import view.CustomButton;
import view.CustomLabel;
import view.CustomComboBox;
import view.CustomTextField;
import view.CustomImageLabel;
import java.util.ArrayList;

public class AccueilView extends JFrame {
    
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
    
    public AccueilView() {
        // Configuration de la fenêtre
        setTitle("UNO Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Initialisation des composants
        initComponents();
        
        // Affichage de la fenêtre
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel = new CustomPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel du haut pour le titre et l'image
        CustomPanel topPanel = new CustomPanel(new BorderLayout(20, 0));
        
        unoImageLabel = new CustomImageLabel();
        try {
            ImageIcon unoImage = new ImageIcon("C:\\Users\\PC\\Downloads\\unogameyousra.jpg");
            Image scaledImage = unoImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            unoImageLabel.setIcon(new ImageIcon(scaledImage));
            topPanel.add(unoImageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image UNO: " + e.getMessage());
        }
        
        titleLabel = new CustomLabel("UNO GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 90));
        titleLabel.setForeground(Color.BLACK);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel du centre pour la configuration des joueurs
        CustomPanel centerPanel = new CustomPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        CustomPanel playerCountPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        playerCountLabel = new CustomLabel("Nombre de joueurs: ");
        playerCountLabel.setForeground(Color.BLACK);
        playerCountPanel.add(playerCountLabel);
        
        String[] playerCounts = {"2", "3", "4"};
        playerCountComboBox = new CustomComboBox<>(playerCounts);
        playerCountComboBox.setSelectedIndex(0);
        playerCountPanel.add(playerCountComboBox);
        
        centerPanel.add(playerCountPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        CustomPanel playersPanel = new CustomPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        
        for (int i = 0; i < 4; i++) {
            playerPanels[i] = new CustomPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            
            playerLabels[i] = new CustomLabel("Joueur " + (i + 1) + ": ");
            playerLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerLabels[i]);
            
            playerNameFields[i] = new CustomTextField(" ", 10);
            playerPanels[i].add(playerNameFields[i]);
            
            playerTypeLabels[i] = new CustomLabel("Type: ");
            playerTypeLabels[i].setForeground(Color.BLACK);
            playerPanels[i].add(playerTypeLabels[i]);
            
            String[] playerTypes = {"Humain", "Robot"};
            playerTypeComboBoxes[i] = new CustomComboBox<>(playerTypes);
            playerTypeComboBoxes[i].setSelectedIndex(i == 0 ? 0 : 1);
            if (i == 0) {
                playerTypeComboBoxes[i].setEnabled(false);
            }
            playerPanels[i].add(playerTypeComboBoxes[i]);
            
            playerIconLabels[i] = new CustomImageLabel();
            updatePlayerIcon(i);
            playerPanels[i].add(playerIconLabels[i]);
            
            playersPanel.add(playerPanels[i]);
            playersPanel.add(Box.createVerticalStrut(10));
        }
        
        updatePlayerPanels();
        centerPanel.add(playersPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        CustomPanel bottomPanel = new CustomPanel(new FlowLayout(FlowLayout.CENTER));
        startGameButton = new CustomButton("Start Game");
        bottomPanel.add(startGameButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void updatePlayerPanels() {
        int playerCount = Integer.parseInt((String) playerCountComboBox.getSelectedItem());
        for (int i = 0; i < playerPanels.length; i++) {
            playerPanels[i].setVisible(i < playerCount);
        }
        revalidate();
        repaint();
    }
    
    public void updatePlayerIcon(int playerIndex) {
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
    
    // 🛠 Getter methods (needed by controller)
    public CustomButton getStartGameButton() {
        return startGameButton;
    }
    
    public CustomComboBox<String> getPlayerCountComboBox() {
        return playerCountComboBox;
    }
    
    public ArrayList<CustomComboBox<String>> getPlayerTypeComboBoxes() {
        ArrayList<CustomComboBox<String>> list = new ArrayList<>();
        for (CustomComboBox<String> comboBox : playerTypeComboBoxes) {
            list.add(comboBox);
        }
        return list;
    }
    
    public ArrayList<CustomTextField> getPlayerNameFields() {
        ArrayList<CustomTextField> list = new ArrayList<>();
        for (CustomTextField textField : playerNameFields) {
            list.add(textField);
        }
        return list;
    }
    
    public void refreshPlayerPanels() {
        updatePlayerPanels();
    }
}
