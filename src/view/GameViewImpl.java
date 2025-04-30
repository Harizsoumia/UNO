 import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JComponent;
import java.util.List;

public class GameViewWithJLayeredPane extends JFrame {

    private GamePanel mainPanel;
    private int numPlayers;
    // Noms par défaut des joueurs
    private String[] playerNames = {"Vous", "Adversaire 1", "Adversaire 2", "Adversaire 3"};

    // Constantes de la vue
    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 700;
    // Dimensions souhaitées pour les cartes affichées
    private static final int CARD_WIDTH = 70;
    private static final int CARD_HEIGHT = 100;
    // Angles pour l'effet éventail (en degrés)
    private static final int FAN_ANGLE = 40; // angle total de l'éventail
    
    // Classe Deck pour gérer le jeu de cartes
    private Deck deck;
    // Stockage de la carte actuelle sur la défausse
    private Card currentDiscardCard;

    public GameViewWithJLayeredPane(int numPlayers, String[] customPlayerNames) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new IllegalArgumentException("Le nombre de joueurs doit être entre 2 et 4.");
        }
        this.numPlayers = numPlayers;

        // Initialiser le deck
        deck = new Deck();

        // Utiliser les noms personnalisés s'ils sont fournis
        if (customPlayerNames != null && customPlayerNames.length >= numPlayers) {
            for (int i = 0; i < numPlayers; i++) {
                this.playerNames[i] = customPlayerNames[i];
            }
        }

        setTitle("Jeu de cartes Uno");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer sur l'écran
      
        mainPanel = new GamePanel();
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        setupLayout();

        // Couleur de fond initiale (tapis de jeu)
        mainPanel.setBackgroundColor(new Color(0, 100, 0)); // Vert foncé comme un tapis de jeu

        setVisible(true);
    }

    // Panneau principal gérant la couleur de fond
    class GamePanel extends JPanel {
        private Color bgColor;

        public void setBackgroundColor(Color color) {
            this.bgColor = color;
            repaint(); // Redessiner le panneau pour appliquer la nouvelle couleur
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (bgColor != null) {
                 g.setColor(bgColor);
                 g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Zone Centrale (Pioche/Défausse) ---
        JPanel centerPanel = setupCenterPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(centerPanel, gbc);

        // --- Panneaux des Joueurs ---
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        // Utiliser Insets pour ajouter un peu d'espace autour des panneaux de main
        gbc.insets = new Insets(5, 5, 5, 5);

        // Joueur 1 (Bas) - Toujours présent (joueur actuel)
        // Simule une main de 7 cartes pour le joueur 1
        JPanel player1Panel = setupHandPanel(0, 7);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.PAGE_END;
        mainPanel.add(player1Panel, gbc);

        // Joueur 2 (Haut) - Toujours présent
        // Simule une main de 7 cartes (dos visible)
        JPanel player2Panel = setupHandPanel(1, 7);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(player2Panel, gbc);

        // Joueur 3 (Gauche) - Conditionnel
        if (numPlayers >= 3) {
             // Simule une main de 5 cartes (dos visible)
            JPanel player3Panel = setupHandPanel(2, 5);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            mainPanel.add(player3Panel, gbc);
        }

        // Joueur 4 (Droite) - Conditionnel
        if (numPlayers == 4) {
             // Simule une main de 6 cartes (dos visible)
            JPanel player4Panel = setupHandPanel(3, 6);
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            mainPanel.add(player4Panel, gbc);
        }
    }

    // Crée le panneau central avec JLayeredPane pour la pioche et la défausse
    private JPanel setupCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // Rendre le panneau transparent pour voir le fond du mainPanel

        // Utiliser JLayeredPane pour la pioche et la défausse
        JLayeredPane centerPane = new JLayeredPane();
        centerPane.setPreferredSize(new Dimension(300, 150));
        centerPane.setOpaque(false); // Rendre le layered pane transparent

        // Pioche (dos de carte)
        JLabel drawPile = createCardLabel(null, "dos");
        drawPile.setBounds(100, 25, CARD_WIDTH, CARD_HEIGHT);
        centerPane.add(drawPile, JLayeredPane.DEFAULT_LAYER);

        // Défausse - avec une carte initiale tirée du deck
        Card cardFromDeck = deck.drawCard(); // Tirer une carte du deck
        if (cardFromDeck != null) {
            currentDiscardCard = cardFromDeck; // Stocker la carte actuelle de la défausse
            JLabel discardPile = createCardLabel(cardFromDeck, "face");
            discardPile.setBounds(200, 25, CARD_WIDTH, CARD_HEIGHT);
            centerPane.add(discardPile, JLayeredPane.DEFAULT_LAYER);
            
            // Mettre à jour la couleur du fond en fonction de la carte
            updateBackgroundColorFromCard(discardPile);
        } else {
            System.out.println("Le deck est vide !");
        }

        // Ajouter le layeredPane au centre du panneau central
        panel.add(centerPane, BorderLayout.CENTER);

        // Ajouter un panneau informatif (tour actuel, direction, etc.)
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false); // Rendre le panneau info transparent
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Ajouter un peu d'espace au-dessus

        JLabel turnLabel = new JLabel("Tour: " + playerNames[0]);
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 14));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel directionLabel = new JLabel("Direction: ↻ Horaire");
        directionLabel.setForeground(Color.WHITE);
        directionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        directionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(turnLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(directionLabel);

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Méthode pour mettre à jour le fond en fonction de la carte jouée sur la défausse
    private void updateBackgroundColorFromCard(JLabel cardLabel) {
        if (cardLabel == null) return;

        // Récupérer la couleur stockée dans les propriétés client
        Card card = (Card) cardLabel.getClientProperty("card");
        if (card == null) return;
        
        Color cardColor = getColorFromCardColor(card.getColor());
        String cardType = card.getValue();

        if (cardColor != null && !cardColor.equals(Color.BLACK) && !cardColor.equals(Color.DARK_GRAY)) {
            // Créer une version plus sombre de la couleur de la carte
            Color bgColor = new Color(
                    Math.max(cardColor.getRed() - 80, 0), // Rendre un peu plus sombre
                    Math.max(cardColor.getGreen() - 80, 0),
                    Math.max(cardColor.getBlue() - 80, 0)
            );
            mainPanel.setBackgroundColor(bgColor);
        } else if (cardColor != null && (cardColor.equals(Color.BLACK) || cardColor.equals(Color.DARK_GRAY)) && !"dos".equals(cardType)) {
             // Si c'est un joker noir joué (pas le dos)
             mainPanel.setBackgroundColor(new Color(0, 80, 0)); // Un vert légèrement différent
        } else if ("dos".equals(cardType)) {
             mainPanel.setBackgroundColor(new Color(0, 100, 0)); // Le vert du tapis de jeu
        } else {
             // Cas d'une carte avec couleur null
             mainPanel.setBackgroundColor(new Color(0, 90, 0)); // Une couleur neutre
        }
    }

    // Méthode pour convertir la couleur de la carte en objet Color
    private Color getColorFromCardColor(String cardColor) {
        if (cardColor == null) return null;
        
        switch (cardColor) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Green": return Color.GREEN;
            case "Yellow": return Color.YELLOW;
            case "Wild": return Color.BLACK;
            default: return Color.GRAY;
        }
    }

    // Méthode pour mettre à jour la défausse avec une nouvelle carte jouée
    public void updateDiscardPile(Card card) {
        // Mémoriser la carte actuelle de la défausse
        currentDiscardCard = card;
        
        // Trouver la défausse dans le panneau central
        Component[] centerComps = mainPanel.getComponents();
        JPanel centerPanel = null;

        // Rechercher le panneau central
        for (Component comp : centerComps) {
            if (comp instanceof JPanel &&
                ((GridBagLayout)mainPanel.getLayout()).getConstraints(comp).gridx == 1 &&
                ((GridBagLayout)mainPanel.getLayout()).getConstraints(comp).gridy == 1) {
                centerPanel = (JPanel)comp;
                break;
            }
        }

        if (centerPanel != null) {
            // Trouver le JLayeredPane contenant la pioche et la défausse
            JLayeredPane centerPane = null;
            for (Component comp : centerPanel.getComponents()) {
                if (comp instanceof JLayeredPane) {
                    centerPane = (JLayeredPane)comp;
                    break;
                }
            }

            if (centerPane != null) {
                // Trouver l'ancienne carte de défausse par sa position
                JLabel oldDiscardCard = null;
                for (Component comp : centerPane.getComponents()) {
                    // On cherche un JLabel à la position de la défausse (200, 25)
                    if (comp instanceof JLabel && comp.getBounds().x == 200 && comp.getBounds().y == 25) {
                        oldDiscardCard = (JLabel) comp;
                        break;
                    }
                }

                // Supprimer l'ancienne carte de défausse si trouvée
                if (oldDiscardCard != null) {
                    centerPane.remove(oldDiscardCard);
                }

                // Créer la nouvelle carte de défausse (face visible)
                JLabel newDiscardCard = createCardLabel(card, "face");

                // Configurer la position de la nouvelle carte sur la défausse
                newDiscardCard.setBounds(200, 25, CARD_WIDTH, CARD_HEIGHT);

                // Ajouter la nouvelle carte à la défausse (au même niveau que l'ancienne)
                centerPane.add(newDiscardCard, JLayeredPane.DEFAULT_LAYER);

                // Mettre à jour la couleur du fond en fonction de la nouvelle carte
                updateBackgroundColorFromCard(newDiscardCard);

                // Forcer la mise à jour de l'affichage
                centerPane.revalidate();
                centerPane.repaint();
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
    }

    // Crée le panneau de main pour un joueur avec JLayeredPane et effet d'éventail
    private JPanel setupHandPanel(int playerIndex, int numCards) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setOpaque(false);

        // Créer un JLayeredPane pour les cartes en éventail
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);

        boolean isHorizontal = (playerIndex == 0 || playerIndex == 1);

        // Calculer la dimension du layeredPane selon l'orientation
        int panelWidth, panelHeight;

        if (isHorizontal) {
            // L'éventail horizontal a besoin de plus d'espace sur les côtés
            panelWidth = CARD_WIDTH * 2 + (numCards > 1 ? (int) ((numCards - 1) * CARD_WIDTH * 0.3) : 0) + 50;
            panelHeight = CARD_HEIGHT + 50; // Espace pour l'arc et le décalage de sélection
        } else {
            // L'éventail vertical
             panelWidth = CARD_WIDTH + 50; // Espace pour l'arc et le décalage de sélection
             panelHeight = CARD_HEIGHT * 2 + (numCards > 1 ? (int) ((numCards - 1) * CARD_HEIGHT * 0.3) : 0) + 50;
        }

        // S'assurer que les dimensions minimales sont respectées
         panelWidth = Math.max(panelWidth, CARD_WIDTH + 50);
         panelHeight = Math.max(panelHeight, CARD_HEIGHT + 50);

        layeredPane.setPreferredSize(new Dimension(panelWidth, panelHeight));
        layeredPane.setMinimumSize(new Dimension(CARD_WIDTH + 50, CARD_HEIGHT + 50)); // Minimum pour que ça s'affiche

        // Distribuer des cartes aux joueurs
        List<Card> playerCards = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            Card drawnCard = deck.drawCard();
            if (drawnCard != null) {
                playerCards.add(drawnCard);
            }
        }

        // Disposer les cartes en éventail
        positionCardsInFan(layeredPane, playerCards, playerIndex);

        // Mettre en place le panneau avec le nom du joueur
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setOpaque(false);
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

        // Affichage du nom du joueur
        JLabel nameLabel = new JLabel(playerNames[playerIndex]);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre de cartes restantes
        JLabel cardCountLabel = new JLabel(playerCards.size() + " cartes");
        cardCountLabel.setForeground(Color.WHITE);
        cardCountLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        cardCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerInfoPanel.add(nameLabel);
        playerInfoPanel.add(Box.createVerticalStrut(2));
        playerInfoPanel.add(cardCountLabel);

        // Placement des éléments selon la position du joueur
        if (playerIndex == 0) { // Joueur en bas
            outerPanel.add(layeredPane, BorderLayout.CENTER);
            outerPanel.add(playerInfoPanel, BorderLayout.SOUTH);
            playerInfoPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Espace au-dessus du texte
        } else if (playerIndex == 1) { // Joueur en haut
            outerPanel.add(layeredPane, BorderLayout.CENTER);
            outerPanel.add(playerInfoPanel, BorderLayout.NORTH);
            playerInfoPanel.setBorder(new EmptyBorder(0, 0, 5, 0)); // Espace en-dessous du texte
        } else if (playerIndex == 2) { // Joueur à gauche
            outerPanel.add(layeredPane, BorderLayout.CENTER);
            outerPanel.add(playerInfoPanel, BorderLayout.WEST);
            playerInfoPanel.setBorder(new EmptyBorder(0, 5, 0, 0)); // Espace à droite du texte
             playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.X_AXIS)); // Disposer nom et count côte à côte
             nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
             cardCountLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
             playerInfoPanel.add(Box.createHorizontalStrut(5)); // Espace entre nom et count
        } else { // Joueur à droite
            outerPanel.add(layeredPane, BorderLayout.CENTER);
            outerPanel.add(playerInfoPanel, BorderLayout.EAST);
             playerInfoPanel.setBorder(new EmptyBorder(0, 0, 0, 5)); // Espace à gauche du texte
             playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.X_AXIS)); // Disposer nom et count côte à côte
             nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
             cardCountLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
             playerInfoPanel.add(Box.createHorizontalStrut(5)); // Espace entre nom et count
        }

        return outerPanel;
    }

    // Disposer les cartes en éventail avec images
    private void positionCardsInFan(JLayeredPane pane, List<Card> cards, int playerIndex) {
        pane.removeAll(); // Nettoyer les cartes précédentes si nécessaire
        if (cards.isEmpty()) {
             pane.revalidate();
             pane.repaint();
             return;
        }

        boolean isHorizontal = (playerIndex == 0 || playerIndex == 1);
        boolean showCardFace = (playerIndex == 0); // Seul le joueur actuel voit ses cartes

        // Centre du panneau où les cartes pivotent
        int pivotX = pane.getPreferredSize().width / 2;
        int pivotY = pane.getPreferredSize().height - CARD_HEIGHT/2 - 10; // Pivot près du bas pour joueur 0
        int verticalPivotX = CARD_WIDTH/2 + 10; // Pivot près de la gauche pour joueur 2
        int verticalPivotY = pane.getPreferredSize().height / 2;

        // Calculer l'angle entre chaque carte
        double angleStep = 0;
        int numCards = cards.size();
        if (numCards > 1) {
            angleStep = FAN_ANGLE / (double)(numCards - 1);
        }

        // Angle de départ (centré)
        double startAngle = -FAN_ANGLE / 2.0;

        for (int i = 0; i < numCards; i++) {
            Card card = cards.get(i);
            
            JLabel cardLabel;
            if (showCardFace) {
                // Afficher la face de la carte pour le joueur actuel
                cardLabel = createCardLabel(card, "face");
            } else {
                // Dos de carte pour les adversaires
                cardLabel = createCardLabel(null, "dos");
            }

            // Calculer l'angle pour cette carte
            double angle = startAngle + (i * angleStep);

            // Appliquer la rotation
            RotatingCardLabel rotatingCardLabel = new RotatingCardLabel(cardLabel.getIcon(), angle, isHorizontal ? 0 : 90);
            
            // Copier les propriétés client du JLabel créé au RotatingCardLabel
            rotatingCardLabel.putClientProperty("card", card);

            // Calculer la position basée sur l'angle
            Point position = new Point();

            if (isHorizontal) {
                // Pour les joueurs en haut et en bas
                 int radius = (int)(pane.getPreferredSize().width * 0.4); // Rayon de l'arc
                 double angleRad = Math.toRadians(angle);
                 position.x = pivotX + (int) (Math.sin(angleRad) * radius);
                 int yOffset = playerIndex == 0 ? 0 : pane.getPreferredSize().height - CARD_HEIGHT - 20; // Ajuster pour le joueur en haut
                 position.y = yOffset + (int) (Math.cos(angleRad) * (radius * 0.3));
                 position.x += (pane.getPreferredSize().width/2) - pivotX - (int)(Math.sin(0) * radius); // Ajuster pour centrer l'éventail
                 position.y += (playerIndex == 0 ? pane.getPreferredSize().height - CARD_HEIGHT - 10 : 10) - yOffset - (int)(Math.cos(0) * (radius * 0.3)); // Ajuster pour placer la base de l'éventail

            } else {
                 // Pour les joueurs à gauche et à droite (éventail vertical)
                 int radius = (int)(pane.getPreferredSize().height * 0.4); // Rayon de l'arc vertical
                 double angleRad = Math.toRadians(angle);
                 position.y = verticalPivotY + (int) (Math.sin(angleRad) * radius);
                 int xOffset = playerIndex == 2 ? 0 : pane.getPreferredSize().width - CARD_WIDTH - 20; // Ajuster pour le joueur à droite
                 position.x = xOffset + (int) (Math.cos(angleRad) * (radius * 0.3));
                 position.y += (pane.getPreferredSize().height/2) - verticalPivotY - (int)(Math.sin(0) * radius);
                 position.x += (playerIndex == 2 ? CARD_WIDTH + 10 : pane.getPreferredSize().width - CARD_WIDTH - 10) - xOffset - (int)(Math.cos(0) * (radius * 0.3));
            }

            // Positionner la carte
            int cardDrawX = position.x - CARD_WIDTH / 2;
            int cardDrawY = position.y - CARD_HEIGHT / 2;

            rotatingCardLabel.setBounds(cardDrawX, cardDrawY, CARD_WIDTH, CARD_HEIGHT);

            // Ajouter au layeredPane
            pane.add(rotatingCardLabel, Integer.valueOf(i));

            // Ajouter un écouteur d'événements pour la carte du joueur actuel
            if (playerIndex == 0) {
                final int cardIndex = i;
                final RotatingCardLabel finalCardLabel = rotatingCardLabel;
                final Point originalLocation = new Point(cardDrawX, cardDrawY);
                final Card finalCard = card;

                rotatingCardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        // Effet de sélection - soulever la carte quand cliquée
                        // Remettre les autres cartes soulevées à leur place d'origine
                        for (Component comp : pane.getComponents()) {
                            if (comp instanceof RotatingCardLabel) {
                                RotatingCardLabel rc = (RotatingCardLabel) comp;
                                if (rc != finalCardLabel) {
                                    Point compOriginalLocation = (Point)rc.getClientProperty("originalLocation");
                                    if (compOriginalLocation != null && rc.getY() < compOriginalLocation.y) {
                                        rc.setLocation(compOriginalLocation.x, compOriginalLocation.y);
                                        pane.repaint();
                                    }
                                }
                            }
                        }

                        // Stocker la position originale si ce n'est pas déjà fait
                        if (finalCardLabel.getClientProperty("originalLocation") == null) {
                            finalCardLabel.putClientProperty("originalLocation", originalLocation);
                        }

                        // Soulever/Baisser la carte cliquée
                        Point currentOriginalLocation = (Point)finalCardLabel.getClientProperty("originalLocation");
                        if (finalCardLabel.getY() == currentOriginalLocation.y) { // Si elle n'est pas déjà soulevée
                            finalCardLabel.setLocation(currentOriginalLocation.x, currentOriginalLocation.y - 15); // Soulever de 15 pixels
                        } else {
                            // Baisser la carte si elle est déjà soulevée
                            finalCardLabel.setLocation(currentOriginalLocation.x, currentOriginalLocation.y);
                        }
                        pane.repaint(); // Redessiner pour le changement de position

                        System.out.println("Carte cliquée - Index: " + cardIndex + ", Valeur: " + finalCard.getValue() + ", Couleur: " + finalCard.getColor());

                        // Simuler le jeu sur la défausse
                        updateDiscardPile(finalCard);

                        // Retirer la carte jouée de la main du joueur (visuellement)
                        pane.remove(finalCardLabel);
                        pane.revalidate();
                        pane.repaint();
                    }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        // Effet de survol - soulève légèrement la carte ou change la bordure
                        Point currentOriginalLocation = (Point)finalCardLabel.getClientProperty("originalLocation");
                        if (currentOriginalLocation != null && finalCardLabel.getY() == currentOriginalLocation.y) {
                            finalCardLabel.setLocation(currentOriginalLocation.x, currentOriginalLocation.y - 5); // Soulève de 5 pixels
                            finalCardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Change la bordure
                            pane.repaint();
                        } else if (currentOriginalLocation == null) {
                            // Cas initial avant le premier clic
                            finalCardLabel.setLocation(finalCardLabel.getX(), finalCardLabel.getY() - 5);
                            finalCardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                            // Stocker la position initiale si ce n'est pas déjà fait
                            if (finalCardLabel.getClientProperty("originalLocation") == null) {
                                finalCardLabel.putClientProperty("originalLocation", new Point(finalCardLabel.getX(), finalCardLabel.getY() + 5));
                            }
                            pane.repaint();
                        } else {
                            // La carte est déjà soulevée par un clic, juste changer la bordure
                            finalCardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                        }
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        // Retour à la normale
                        finalCardLabel.setBorder(BorderFactory.createEmptyBorder()); // Supprimer la bordure
                        // On ne remet à la place d'origine que si elle a été soulevée par survol (5px)
                        Point currentOriginalLocation = (Point)finalCardLabel.getClientProperty("originalLocation");
                        // On vérifie si la position actuelle est celle du survol (original Y - 5)
                        if (currentOriginalLocation != null && finalCardLabel.getY() == currentOriginalLocation.y - 5) {
                            finalCardLabel.setLocation(currentOriginalLocation.x, currentOriginalLocation.y);
                            pane.repaint();
                        }
                    }
                });
            }
        }

        // Forcer le layout et le dessin après avoir ajouté toutes les cartes
        pane.revalidate();
        pane.repaint();
    }

    // Crée un JLabel pour représenter une carte
    private JLabel createCardLabel(Card card, String displayType) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        label.setOpaque(false);
        
        // Simuler l'apparence d'une carte
        // Dans une implémentation réelle, vous chargeriez ici les images des cartes
        if ("dos".equals(displayType)) {
            // Dos de carte (carte cachée)
            label.setIcon(createCardIcon("", Color.BLUE, "dos"));
label.setIcon(createCardIcon("", Color.BLUE, "dos"));
        } else {
            // Face de carte (carte visible)
            if (card != null) {
                label.setIcon(createCardIcon(card.getValue(), getColorFromCardColor(card.getColor()), "face"));
                // Stocker la référence de la carte dans les propriétés client pour y accéder plus tard
                label.putClientProperty("card", card);
            } else {
                // Fallback au cas où la carte est null
                label.setIcon(createCardIcon("?", Color.GRAY, "face"));
            }
        }
        
        return label;
    }

    // Crée un Icon pour représenter une carte (dos ou face)
    private Icon createCardIcon(String value, Color color, String type) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Activer l'anti-aliasing pour un meilleur rendu
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dessiner le fond de la carte (rectangle arrondi blanc)
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(x, y, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
                
                // Dessiner la bordure
                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(x, y, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
                
                if ("dos".equals(type)) {
                    // Dessiner le dos de carte (motif)
                    g2d.setColor(color);
                    g2d.fillRoundRect(x + 5, y + 5, CARD_WIDTH - 11, CARD_HEIGHT - 11, 5, 5);
                    
                    // Logo UNO au milieu
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 18));
                    FontMetrics fm = g2d.getFontMetrics();
                    String unoText = "UNO";
                    int textWidth = fm.stringWidth(unoText);
                    g2d.drawString(unoText, x + (CARD_WIDTH - textWidth) / 2, y + CARD_HEIGHT / 2 + fm.getAscent() / 2);
                } else {
                    // Dessiner la face de la carte
                    // Zone colorée centrale
                    g2d.setColor(color);
                    g2d.fillOval(x + 5, y + 5, CARD_WIDTH - 10, CARD_HEIGHT - 10);
                    
                    // Diagonale blanche symbolique du UNO
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(x + 10, y + CARD_HEIGHT / 2 - 7, CARD_WIDTH - 20, 14, 5, 5);
                    g2d.setColor(color);
                    g2d.drawString(value, x + CARD_WIDTH / 2 - 5, y + CARD_HEIGHT / 2 + 5);
                    
                    // Valeur en haut à gauche et en bas à droite
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    g2d.drawString(value, x + 3, y + 12);
                    g2d.drawString(value, x + CARD_WIDTH - 15, y + CARD_HEIGHT - 5);
                }
                
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return CARD_WIDTH;
            }
            
            @Override
            public int getIconHeight() {
                return CARD_HEIGHT;
            }
        };
    }

    // Classe pour afficher une carte avec rotation
    class RotatingCardLabel extends JLabel {
        private double angle; // Angle de rotation en degrés
        private int baseRotation; // Rotation de base (0 pour horizontal, 90 pour vertical)
        
        public RotatingCardLabel(Icon icon, double angle, int baseRotation) {
            super(icon);
            this.angle = angle;
            this.baseRotation = baseRotation;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Activer l'anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Calculer le centre de la carte
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            // Appliquer la rotation
            AffineTransform oldTransform = g2d.getTransform();
            g2d.rotate(Math.toRadians(angle + baseRotation), centerX, centerY);
            
            // Dessiner l'icône
            if (getIcon() != null) {
                getIcon().paintIcon(this, g2d, 0, 0);
            }
            
            // Restaurer la transformation
            g2d.setTransform(oldTransform);
            
            // Dessiner la bordure si présente
            if (getBorder() != null) {
                getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
            }
            
            g2d.dispose();
        }
    }

    // Classe de test main pour démarrer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test avec deux joueurs
            String[] customNames = {"Joueur", "CPU 1", "CPU 2", "CPU 3"};
            new GameViewWithJLayeredPane(3, customNames);
        });
    }
}




// ======================================================pour texter 
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

   // --- Main ---
    // Pour tester le jeu
 
    // Point d'entrée pour exécuter la vue
public class UnoGui{












    public static void main(String[] args) {
        // Créer une fenêtre d'options de démarrage
        showStartupOptions();
    }
    
    private static void showStartupOptions() {
        // Créer une boîte de dialogue pour les options
        JFrame optionsFrame = new JFrame("Configuration du jeu Uno");
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setSize(400, 300);
        optionsFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Choix du nombre de joueurs
        JPanel playerCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerCountPanel.add(new JLabel("Nombre de joueurs:"));
        JComboBox<Integer> playerCountBox = new JComboBox<>(new Integer[]{2, 3, 4});
        playerCountBox.setSelectedItem(3); // Par défaut: 3 joueurs
        playerCountPanel.add(playerCountBox);
        panel.add(playerCountPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Champs pour les noms des joueurs
        JPanel namesPanel = new JPanel();
        namesPanel.setLayout(new GridLayout(4, 2, 5, 5));
        namesPanel.add(new JLabel("Votre nom:"));
        JTextField player1Field = new JTextField("Vous", 15);
        namesPanel.add(player1Field);
        
        namesPanel.add(new JLabel("Joueur 2:"));
        JTextField player2Field = new JTextField("Adversaire 1", 15);
        namesPanel.add(player2Field);
        
        namesPanel.add(new JLabel("Joueur 3:"));
        JTextField player3Field = new JTextField("Adversaire 2", 15);
        namesPanel.add(player3Field);
        
        namesPanel.add(new JLabel("Joueur 4:"));
        JTextField player4Field = new JTextField("Adversaire 3", 15);
        namesPanel.add(player4Field);
        
        panel.add(namesPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Bouton de démarrage
        JButton startButton = new JButton("Démarrer le jeu");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            // Récupérer les valeurs choisies
            int numPlayers = (Integer) playerCountBox.getSelectedItem();
            String[] playerNames = new String[4];
            playerNames[0] = player1Field.getText();
            playerNames[1] = player2Field.getText();
            playerNames[2] = player3Field.getText();
            playerNames[3] = player4Field.getText();
            
            // Fermer la fenêtre d'options
            optionsFrame.dispose();
            
            // Lancer le jeu avec les options choisies
            SwingUtilities.invokeLater(() -> {
                new GameViewWithJLayeredPane(numPlayers, playerNames);
            });
        });
        
        panel.add(startButton);
        
        optionsFrame.add(panel);
        optionsFrame.setVisible(true);
    }

}

