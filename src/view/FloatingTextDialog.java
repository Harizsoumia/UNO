package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * FloatingTextDialog - Creates a floating, temporary dialog with animated text
 * Used for displaying game events and special card effects
 */
public class FloatingTextDialog extends JDialog {
    
    private JLabel textLabel;
    private Timer fadeTimer;
    private float opacity = 1.0f;
    
    /**
     * Creates a new floating text dialog
     * 
     * @param message The text to display
     */
    public FloatingTextDialog(String message) {
        // Set up dialog properties
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background
        setModal(false);
        setAlwaysOnTop(true);
        
        // Create and style the text label
        textLabel = new JLabel(message);
        textLabel.setFont(new Font("Arial", Font.BOLD, 28));
        textLabel.setForeground(new Color(255, 255, 255));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Add shadow effect
        textLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 100), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            )
        ));
        
        // Set content pane with semi-transparent background
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * 0.7f));
                g2d.setColor(new Color(40, 60, 80));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        contentPane.setLayout(new BorderLayout());
        contentPane.setOpaque(false);
        contentPane.add(textLabel, BorderLayout.CENTER);
        setContentPane(contentPane);
        
        // Size and position the dialog
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(screenSize.width - 100, 400), 100);
        setLocationRelativeTo(null); // Center on screen
    }
    
    /**
     * Shows the dialog for a specified duration in milliseconds and then fades out
     * 
     * @param durationMs The duration to display the dialog in milliseconds
     */
    public void showFor(int durationMs) {
        // Start fade timer
        fadeTimer = new Timer(50, new ActionListener() {
            private int elapsed = 0;
            private boolean fadeOutStarted = false;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsed += 50;
                
                // Start fade out when 2/3 of the time has elapsed
                if (elapsed >= durationMs * 2/3 && !fadeOutStarted) {
                    fadeOutStarted = true;
                }
                
                // Fade out effect
                if (fadeOutStarted) {
                    opacity -= 0.05f;
                    if (opacity <= 0) {
                        opacity = 0;
                        fadeTimer.stop();
                        dispose(); // Close the dialog
                        return;
                    }
                    repaint();
                }
                
                // Ensure dialog is disposed after the duration
                if (elapsed >= durationMs) {
                    fadeTimer.stop();
                    dispose();
                }
            }
        });
        
        // Slight animation when appearing
        setOpacity(0.0f);
        setVisible(true);
        
        // Fade in animation
        Timer fadeInTimer = new Timer(30, new ActionListener() {
            private float fadeInOpacity = 0.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                fadeInOpacity += 0.1f;
                if (fadeInOpacity >= 1.0f) {
                    fadeInOpacity = 1.0f;
                    ((Timer)e.getSource()).stop();
                    fadeTimer.start(); // Start the main timer after fade-in
                }
                setOpacity(fadeInOpacity);
            }
        });
        
        fadeInTimer.start();
    }
    
    /**
     * Set the color theme of the floating text
     * 
     * @param background The background color
     * @param foreground The text color
     */
    public void setColorTheme(Color background, Color foreground) {
        textLabel.setForeground(foreground);
        repaint();
    }
}