package edu.school21.game;

import edu.school21.chase.MovementDirection;
import edu.school21.game.entities.GameField;
import edu.school21.game.exceptions.GameGenerateException;
import edu.school21.game.exceptions.IllegalParametersException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, GameGenerateException, IllegalParametersException {
        Integer[][] field = {
                {0, 0, 3},
                {1, 0, 0},
                {0, 0, 2}};
        Game game = new Game(5, 20, 20, 20, "application-production.properties");
        game.start(System.in);
    }
}