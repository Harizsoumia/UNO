package view;

/**
 *
 * @author PC
 */

// CustomImageLabel.java
import javax.swing.*;
import java.awt.*;

public class CustomImageLabel extends JLabel {
    public CustomImageLabel() {
        super();
    }
    
    public CustomImageLabel(String imagePath) {
        super();
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledImage = icon.getImage().getScaledInstance(0, 200, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
        }
    }
    
    public CustomImageLabel(String imagePath, int width, int height) {
        super();
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
        }
    }
}