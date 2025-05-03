package viewunogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.CustomPanel;
import view.CustomLabel;
import view.CustomTextField;
import view.CustomRestartButton;
import view.CustomFrame;

/**
 * WinnerView - Page d'affichage du gagnant du jeu UNO
 * utilisant uniquement des composants personnalisés, sans classes internes
 */
public class WinnerView extends CustomFrame {
    
    // Composants de l'interface
    private CustomPanel mainPanel;
    private CustomLabel titleLabel;
    private CustomTextField winnerNameField;
    private CustomRestartButton restartButton;
    
    /**
     * Constructeur de la page du gagnant
     */
    public WinnerView() {
        // Configuration de la fenêtre
        super("UNO Game - Winner");
        applyTheme("uno"); // Applique le thème UNO
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Initialisation des composants
        initComponents();
        
        // Affichage de la fenêtre
        setVisible(true);
    }
    
    /**
     * Constructeur avec le nom du gagnant
     */
    public WinnerView(String winnerName) {
        this();
        setWinnerName(winnerName);
    }
    
    /**
     * Initialise les composants de l'interface
     */
    private void initComponents() {
        // Création du panel principal
        mainPanel = new CustomPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Panel pour le titre
        CustomPanel titlePanel = new CustomPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        titleLabel = new CustomLabel("THE WINNER IS:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Panel pour le nom du gagnant
        CustomPanel winnerPanel = new CustomPanel();
        winnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        winnerNameField = new CustomTextField("", 20);
        winnerNameField.setFont(new Font("Arial", Font.BOLD, 30));
        winnerNameField.setHorizontalAlignment(JTextField.CENTER);
        winnerNameField.setEditable(false);
        winnerNameField.setBackground(Color.WHITE);
        winnerNameField.setForeground(new Color(148, 0, 211)); // Violet foncé
        
        winnerPanel.add(winnerNameField);
        
        mainPanel.add(winnerPanel);
        mainPanel.add(Box.createVerticalStrut(50));
        
        // Panel pour le bouton de redémarrage
        CustomPanel buttonPanel = new CustomPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        restartButton = new CustomRestartButton("RESTART GAME");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setPreferredSize(new Dimension(200, 50));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        
        buttonPanel.add(restartButton);
        
        mainPanel.add(buttonPanel);
        
        // Création d'un panel pour les confettis (effet visuel pour le gagnant)
        CustomPanel confettiPanel = new CustomPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawConfetti(g);
            }
        };
        confettiPanel.setOpaque(false);
        
        // Ajout du panel principal à la fenêtre avec l'effet de confettis
        setContentPane(mainPanel);
    }
    
    /**
     * Dessine des confettis pour célébrer la victoire
     */
    private void drawConfetti(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        
        // Couleurs des confettis (couleurs UNO)
        Color[] colors = {
            new Color(220, 53, 53),   // Rouge
            new Color(0, 111, 185),   // Bleu
            new Color(255, 185, 0),   // Jaune
            new Color(0, 166, 81)     // Vert
        };
        
        // Dessiner 100 confettis aléatoires
        for (int i = 0; i < 100; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            int size = (int) (Math.random() * 10) + 5;
            Color color = colors[(int) (Math.random() * colors.length)];
            
            g2d.setColor(color);
            
            // Formes variées (cercles, rectangles, triangles)
            int shape = (int) (Math.random() * 3);
            switch (shape) {
                case 0: // Cercle
                    g2d.fillOval(x, y, size, size);
                    break;
                case 1: // Rectangle
                    g2d.fillRect(x, y, size, size);
                    break;
                case 2: // Triangle
                    int[] xPoints = {x, x + size / 2, x + size};
                    int[] yPoints = {y + size, y, y + size};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }
    }
    
    /**
     * Définit le nom du gagnant
     */
    public void setWinnerName(String name) {
        winnerNameField.setText(name);
    }
    
    /**
     * Retourne le bouton de redémarrage
     */
    public CustomRestartButton getRestartButton() {
        return restartButton;
    }
    
    /**
     * Redémarre le jeu en fermant cette fenêtre et ouvrant une nouvelle fenêtre d'accueil
     */
    private void restartGame() {
        // Fermer cette fenêtre
        dispose();
        
        // Ouvrir la page d'accueil
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AccueilView();
            }
        });
    }
    
    /**
     * Méthode principale pour tester la page du gagnant
     */
    public static void main(String[] args) {
        // Utiliser SwingUtilities pour s'assurer que l'interface est créée dans l'EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WinnerView("Player 1");
            }
        });
    }
}
