// CustomButton.java
package view;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(new Color(255, 255, 0));
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setBorderPainted(true);
        setContentAreaFilled(true);

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                setBackground(new Color(255, 255, 100));
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                setBackground(new Color(255, 255, 0));
            }
        });
    }
}
