package view;

import java.awt.*;
import javax.swing.*;

public class VerticalPileLayout implements LayoutManager {

    private int overlap = 25; // chevauchement vertical

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int n = parent.getComponentCount();
        if (n == 0) return new Dimension(0, 0);
        Component comp = parent.getComponent(0);
        Dimension d = comp.getPreferredSize();
        int width = d.width;
        int height = d.height + (n - 1) * overlap;
        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        int n = parent.getComponentCount();
        if (n == 0) return;
        Component comp = parent.getComponent(0);
        Dimension d = comp.getPreferredSize();
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            c.setBounds(0, i * overlap, d.width, d.height);
        }
    }
}
