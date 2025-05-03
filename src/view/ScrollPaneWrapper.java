package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * ScrollPaneWrapper - A custom JScrollPane with styling specific to the UNO game
 * This wrapper provides consistent styling and behavior for scrollable areas
 */
public class ScrollPaneWrapper extends JScrollPane {
    
    // Constants for scrollbar policies (matching JScrollPane constants for convenience)
    public static final int VERTICAL_SCROLLBAR_AS_NEEDED = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
    public static final int VERTICAL_SCROLLBAR_NEVER = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
    public static final int VERTICAL_SCROLLBAR_ALWAYS = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
    public static final int HORIZONTAL_SCROLLBAR_AS_NEEDED = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    public static final int HORIZONTAL_SCROLLBAR_NEVER = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
    public static final int HORIZONTAL_SCROLLBAR_ALWAYS = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
    
    /**
     * Creates a new ScrollPaneWrapper with custom styling
     * 
     * @param view The component to be displayed in the scroll pane
     */
    public ScrollPaneWrapper(Component view) {
        super(view);
        initializeStyle();
    }
    
    /**
     * Creates a new ScrollPaneWrapper with custom styling and specified scrollbar policies
     * 
     * @param view The component to be displayed in the scroll pane
     * @param vsbPolicy The vertical scrollbar policy
     * @param hsbPolicy The horizontal scrollbar policy
     */
    public ScrollPaneWrapper(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        initializeStyle();
    }
    
    /**
     * Initialize the styling for the scroll pane
     */
    private void initializeStyle() {
        // Set the styling for the scroll pane
        setOpaque(false);
        getViewport().setOpaque(false);
        
        // Customize the scrollbar appearance
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        
        // Set custom colors
        horizontalScrollBar.setBackground(new Color(30, 50, 70));
        horizontalScrollBar.setForeground(new Color(70, 130, 180));
        verticalScrollBar.setBackground(new Color(30, 50, 70));
        verticalScrollBar.setForeground(new Color(70, 130, 180));
        
        // Adjust scrollbar UI
        UIManager.put("ScrollBar.thumb", new Color(70, 130, 180));
        UIManager.put("ScrollBar.track", new Color(30, 50, 70));
        
        // Customize the scrollbar size
        horizontalScrollBar.setPreferredSize(new Dimension(0, 10));
        verticalScrollBar.setPreferredSize(new Dimension(10, 0));
        
        // Set default border
        setBorder(BorderFactory.createEmptyBorder());
    }
    
    /**
     * Overridden to maintain the background transparency
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Paint with transparency
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent background
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        super.paintComponent(g);
    }
    
    /**
     * Adds uniform padding around the viewport
     * 
     * @param padding The amount of padding in pixels
     */
    public void setPadding(int padding) {
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
    
    /**
     * Makes the scrollbars always show their arrows for easier navigation
     */
    public void setAlwaysShowArrows() {
        horizontalScrollBar.setUnitIncrement(20);
        verticalScrollBar.setUnitIncrement(20);
    }
    
    /**
     * Sets the scroll increment (how much to scroll per arrow click)
     * 
     * @param increment The number of pixels to scroll
     */
    public void setScrollIncrement(int increment) {
        getHorizontalScrollBar().setUnitIncrement(increment);
        getVerticalScrollBar().setUnitIncrement(increment);
    }
    
    /**
     * Makes the scrollbars appear when needed and fade out when not in use
     */
    public void enableAutoHideScrollbars() {
        setWheelScrollingEnabled(true);
        // This would require additional timer-based animations for a proper fade effect
        // Basic implementation just uses the as-needed policy
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }
}