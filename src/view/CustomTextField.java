// CustomTextField.java
package view;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns);
        setupStyle();
    }

    public CustomTextField(String text, int columns) {
        super(text, columns);
        setupStyle();
    }
    
    private void setupStyle() {
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(new Color(148, 0, 211), 1));
    }
}
