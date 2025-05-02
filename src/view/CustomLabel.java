package view;

/**
 *
 * @author PC
 */

// CustomLabel.java
import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {
    public CustomLabel(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(new Color(50, 50, 50));
    }
    
    public CustomLabel(String text, int alignment) {
        super(text, alignment);
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(new Color(50, 50, 50));
    }
    
    public CustomLabel() {
        super();
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(new Color(50, 50, 50));
    }
}