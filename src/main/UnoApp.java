package main;

import java.util.ArrayList;
import java.util.List;

import controller.GameController;
import unoGamepackage.Game;
import unoGamepackage.Player;
import view.GameViewImpl;

public class UnoApp {
    public static void main(String[] args) {
        // Create the model (Game)
        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        Game model = new Game(players);

        // Create the view
        GameViewImpl view = new GameViewImpl();

        // Create the controller
        GameController controller = new GameController(view, model);

        // Set the controller in the view
        view.setController(controller);

        // Start the game
        controller.startGame();
    }
}
