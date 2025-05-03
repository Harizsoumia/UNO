package view;

import javax.swing.*;
import java.awt.*;

/**
 * DialogHelper - Utility class for creating consistent dialog boxes in the UNO game
 * This class wraps the standard JOptionPane methods with custom styling and functionality
 */
public class DialogHelper {

    // Dialog type constants (matching JOptionPane constants for convenience)
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;

    /**
     * Shows a message dialog with custom styling
     * 
     * @param parentComponent the parent component for the dialog
     * @param message the message to display
     */
    public static void showMessageDialog(Component parentComponent, String message) {
        showMessageDialog(parentComponent, message, "UNO Game", INFORMATION_MESSAGE);
    }

    /**
     * Shows a message dialog with custom styling and specified title and type
     * 
     * @param parentComponent the parent component for the dialog
     * @param message the message to display
     * @param title the title for the dialog window
     * @param messageType the type of message (ERROR_MESSAGE, INFORMATION_MESSAGE, etc.)
     */
    public static void showMessageDialog(Component parentComponent, String message, 
                                         String title, int messageType) {
        // Apply custom styling to the option pane
        UIManager.put("OptionPane.background", new Color(40, 60, 80));
        UIManager.put("Panel.background", new Color(40, 60, 80));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        
        // Create a custom styled dialog
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        
        // We'll just use a default icon since UnoGameIcons might not exist
        // dialog.setIconImage(UnoGameIcons.getAppIcon());
        
        // Show dialog and wait for it to close
        dialog.setVisible(true);
        
        // Reset UI manager properties to avoid affecting other components
        resetUIManagerProperties();
    }

    /**
     * Shows an input dialog with custom styling
     * 
     * @param parentComponent the parent component for the dialog
     * @param message the message to display
     * @param title the title for the dialog window
     * @param messageType the type of message (QUESTION_MESSAGE, etc.)
     * @param icon the icon to display (can be null)
     * @param selectionValues the possible values to choose from
     * @param initialSelectionValue the initially selected value
     * @return the selected value or null if canceled
     */
    public static Object showInputDialog(Component parentComponent, String message, 
                                          String title, int messageType, Icon icon,
                                          Object[] selectionValues, Object initialSelectionValue) {
        // Apply custom styling to the option pane
        UIManager.put("OptionPane.background", new Color(40, 60, 80));
        UIManager.put("Panel.background", new Color(40, 60, 80));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(70, 130, 180));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        
        // Create and show the input dialog
        Object selectedValue = JOptionPane.showInputDialog(
            parentComponent,
            message,
            title,
            messageType,
            icon,
            selectionValues,
            initialSelectionValue
        );
        
        // Reset UI manager properties to avoid affecting other components
        resetUIManagerProperties();
        
        return selectedValue;
    }
    
    /**
     * Shows a confirmation dialog with Yes/No options
     * 
     * @param parentComponent the parent component for the dialog
     * @param message the message to display
     * @param title the title for the dialog window
     * @return JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
     */
    public static int showConfirmDialog(Component parentComponent, String message, String title) {
        // Apply custom styling to the option pane
        UIManager.put("OptionPane.background", new Color(40, 60, 80));
        UIManager.put("Panel.background", new Color(40, 60, 80));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        
        // Create and show the confirmation dialog
        int result = JOptionPane.showConfirmDialog(
            parentComponent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            QUESTION_MESSAGE
        );
        
        // Reset UI manager properties to avoid affecting other components
        resetUIManagerProperties();
        
        return result;
    }
    
    /**
     * Resets the UI Manager properties to their defaults
     * This prevents the custom styling from affecting other components
     */
    private static void resetUIManagerProperties() {
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("ComboBox.background", null);
        UIManager.put("ComboBox.foreground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
    }
}