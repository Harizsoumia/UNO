package viewunogame;

import view.CustomPanel;
import view.CustomLabel;
import view.CustomTextField;
import view.CustomRestartButton;





/**
 * WinnerView - Page d'affichage du gagnant du jeu UNO
 * utilisant uniquement des composants personnalisés, sans classes internes
 */
public class WinnerView extends JFrame {
    
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
        setTitle("UNO Game - Winner");
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
        winnerNameField.setForeground(new Color(148, 0, 211));
        
        winnerPanel.add(winnerNameField);
        
        mainPanel.add(winnerPanel);
        mainPanel.add(Box.createVerticalStrut(50));
        
        // Panel pour le bouton de redémarrage
        CustomPanel buttonPanel = new CustomPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        restartButton = new CustomRestartButton("RESTART GAME");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setPreferredSize(new Dimension(200, 50));
        
        buttonPanel.add(restartButton);
        
        mainPanel.add(buttonPanel);
        
        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);
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
     * Méthode principale pour tester la page du gagnant
     */
    public static void main(String[] args) {
        // Utiliser SwingUtilities pour s'assurer que l'interface est créée dans l'EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WinnerView("");
            }
        });
    }
}