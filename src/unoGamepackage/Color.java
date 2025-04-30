package unoGamepackage;

public enum Color {
    RED, GREEN, BLUE, YELLOW, WILD;

    // Convert from AWT Color to UNO Color
    public static Color fromAwtColor(java.awt.Color awtColor) {
        if (awtColor.equals(java.awt.Color.RED)) {
            return RED;
        } else if (awtColor.equals(java.awt.Color.GREEN)) {
            return GREEN;
        } else if (awtColor.equals(java.awt.Color.BLUE)) {
            return BLUE;
        } else if (awtColor.equals(java.awt.Color.YELLOW)) {
            return YELLOW;
        } else {
            return WILD;
        }
    }

    // Convert from UNO Color to AWT Color
    public java.awt.Color toAwtColor() {
        return switch (this) {
            case RED -> java.awt.Color.RED;
            case GREEN -> java.awt.Color.GREEN;
            case BLUE -> java.awt.Color.BLUE;
            case YELLOW -> java.awt.Color.YELLOW;
            case WILD -> java.awt.Color.BLACK; // or another default for wild
        };
    }
}
