// CustomComboBox.java
package view;

import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;

public class CustomComboBox<E> extends JComboBox<E> {
    public CustomComboBox(E[] items) {
        super(items);
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(new Color(148, 0, 211), 1));
    }
}