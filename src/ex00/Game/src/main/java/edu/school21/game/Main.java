package edu.school21.game;

import edu.school21.game.entities.GameField;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game(10,50,20, "application-production.properties");
        game.render();
    }
}