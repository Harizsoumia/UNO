package view;

import javax.swing.*;
import java.awt.*;
import model.Card;

/**
 * CustomComponent for displaying UNO cards
 */
public class CardComponent extends JPanel {
    private Card card;
    private boolean isBackSide;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 150;
    
    /**
     * Constructor for front-facing card
     * @param card The card to display
     */
    public CardComponent(Card card) {
        this.card = card;
        this.isBackSide = false;
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setOpaque(true);
    }
    
    /**
     * Constructor with option to show back side of card
     * @param card The card to display (can be null for draw pile)
     * @param isBackSide True to show back of card
     */
    public CardComponent(Card card, boolean isBackSide) {
        this.card = card;
        this.isBackSide = isBackSide;
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setOpaque(true);
    }
    
    /**
     * Set the card to display
     * @param card The card
     */
    public void setCard(Card card) {
        this.card = card;
        repaint();
    }
    
    /**
     * Set whether to show back side of card
     * @param isBackSide True to show back
     */
    public void setBackSide(boolean isBackSide) {
        this.isBackSide = isBackSide;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother drawing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw card background
        if (isBackSide) {
            // Draw card back
            g2d.setColor(Color.BLACK);
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRoundRect(4, 4, getWidth() - 9, getHeight() - 9, 6, 6);
            
            // Draw UNO text
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "UNO";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2 + textHeight / 4);
        } else if (card != null) {
            // Draw card face
            Color cardColor;
            switch (card.getColor()) {
                case RED:
                    cardColor = Color.RED;
                    break;
                case BLUE:
                    cardColor = Color.BLUE;
                    break;
                case GREEN:
                    cardColor = Color.GREEN;
                    break;
                case YELLOW:
                    cardColor = Color.YELLOW;
                    break;
                default:
                    cardColor = Color.BLACK;
                    break;
            }
            
            // Draw card background
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2d.setColor(cardColor);
            g2d.fillRoundRect(4, 4, getWidth() - 9, getHeight() - 9, 6, 6);
            
            // Draw card value
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g2d.getFontMetrics();
            String text = card.getValue().toString();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            
            // Draw in center
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2 + textHeight / 4);
            
            // Draw smaller value in corners
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(text, 5, 15);
            g2d.drawString(text, getWidth() - 25, getHeight() - 5);
        } else {
            // Empty card slot or placeholder
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        }
    }
}