package view;

import java.awt.Color;
import java.awt.Font;

/**
 * Custom button specifically for restart functionality
 * @author PC
 */
public class CustomRestartButton extends CustomButton {
    
    public CustomRestartButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(new Color(255, 102, 0)); // Orange color for restart buttons
        setForeground(Color.WHITE);
        
        // Override the hover effect
        removeMouseListener(getMouseListeners()[0]); // Remove default hover effect
        
        // Add custom hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(255, 153, 51)); // Lighter orange on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(255, 102, 0)); // Back to original orange
            }
        });
    }
}