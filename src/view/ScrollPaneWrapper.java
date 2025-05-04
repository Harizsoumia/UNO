package view;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneWrapper extends JScrollPane {

    public ScrollPaneWrapper(Component view) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

        // Optionnel : style transparent pour la barre de scroll
        JScrollBar hBar = getHorizontalScrollBar();
        hBar.setPreferredSize(new Dimension(0, 12)); // hauteur réduite
        hBar.setUnitIncrement(20); // vitesse de scroll
        hBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 180, 180, 100);
                this.trackColor = new Color(0, 0, 0, 0);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }
}
