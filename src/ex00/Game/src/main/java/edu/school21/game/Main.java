package edu.school21.game;

import edu.school21.chase.MovementDirection;
import edu.school21.game.entities.GameField;
import edu.school21.game.exceptions.GameGenerateException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, GameGenerateException {
        Game game = new Game(1,40,12, 12, "application-production.properties");
        game.start();
    }
}