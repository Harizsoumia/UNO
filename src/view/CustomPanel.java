package view;



import javax.swing.JPanel;
import java.awt.Color;
import java.awt.LayoutManager;

public class CustomPanel extends JPanel {
    public CustomPanel() {
        setBackground(new Color(148, 0, 211));
    }

    public CustomPanel(LayoutManager layout) {
        super(layout);
        setBackground(new Color(148, 0, 211));
    }
}
