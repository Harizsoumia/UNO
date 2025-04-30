package unoGamepackage;
import javax.swing.*;
import java.io.File;

public class Card {
    private String color;
    private String value;
    private String imageName;
    private ImageIcon image;

    // Nouveau chemin avec le bon dossier + barre finale
    private static final String IMAGE_PATH = "C:\\Users\\manel\\OneDrive\\Bureau\\cartesuno\\cards\\";

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
        this.imageName = generateImageName();
        this.image = loadImage();
    }
     // Taille des cartes redimensionnées (ex: 100x150 pixels)
    private static final int CARD_WIDTH = 50;
    private static final int CARD_HEIGHT = 50;

    // Génère le bon nom d’image avec .jpg
    private String generateImageName() {
        if (color.equalsIgnoreCase("Wild")) {
            return value + ".jpg"; // ex : "Wild.jpg"
        } else {
            return color + "_" + value + ".jpg"; // ex : "Blue_5.jpg"
        }
    }

    // Charge l'image depuis le chemin complet
    private ImageIcon loadImage() {
        String fullPath = IMAGE_PATH + imageName;
        File imageFile = new File(fullPath);
        if (imageFile.exists()) {
            return new ImageIcon(fullPath);
        } else {
            System.err.println("Image non trouvée : " + fullPath);
            return null;
        }
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public ImageIcon getImage() {
        return image;
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}