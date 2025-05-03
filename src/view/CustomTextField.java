package view;

/**
 *
 * @author PC
 */

// CustomTextField.java
import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns);
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(new Color(148, 0, 211), 1));
    }
    
    public CustomTextField(String text, int columns) {
        super(text, columns);
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(new Color(148, 0, 211), 1));
    }
}