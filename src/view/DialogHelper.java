package view;

import java.awt.*;
import javax.swing.*;

public class PileLayout implements LayoutManager {

    private int overlap = 30; // chevauchement horizontal

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
        int width = d.width + (n - 1) * overlap;
        int height = d.height;
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
            c.setBounds(i * overlap, 0, d.width, d.height);
        }
    }
}
