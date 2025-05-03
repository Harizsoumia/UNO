package main;

import model.Card;
import model.Player;
import model.UnoGame;
import viewunogame.GameBoardPage;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Create players
        Player player1 = new Player("Player 1", Player.PlayerType.HUMAN);
        Player player2 = new Player("Player 2", Player.PlayerType.BOT);
        Player player3 = new Player("Player 3", Player.PlayerType.HUMAN);
        Player player4 = new Player("Player 4", Player.PlayerType.BOT);

        // Add players to the game
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        // Create UnoGame instance
        UnoGame game = new UnoGame();  // Use default constructor

        // Initialize the game with the players
        game.initializeGame(players);  // Use this method to initialize the game

        // Set up the JFrame and the game board page
        JFrame frame = new JFrame("UNO Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create and add GameBoardPage to the frame
        GameBoardPage gameBoardPage = new GameBoardPage(game);
        frame.add(gameBoardPage);

        // Show the window
        frame.setVisible(true);
    }
}
