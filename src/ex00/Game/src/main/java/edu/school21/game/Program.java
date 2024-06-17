package edu.school21.game;

import com.beust.ah.A;
import com.beust.jcommander.JCommander;
import edu.school21.game.Args.Args;
import edu.school21.game.exceptions.GameGenerateException;
import edu.school21.game.exceptions.IllegalParametersException;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException, GameGenerateException, IllegalParametersException {
        Args argsForProgram = new Args();
        JCommander commander = new JCommander(argsForProgram);
        commander.parse(args);
        Integer enemiesCount = argsForProgram.getEnemiesCount();
        Integer wallsCount = argsForProgram.getWallsCount();
        Integer size = argsForProgram.getSize();
        String profile = argsForProgram.getProfile();
        Game game = new Game(enemiesCount, wallsCount, size, size, "application-production.properties");
        if (profile != null && profile.startsWith("dev")) {
            game.devModeOn("application-dev.properties");
        }
        game.start(System.in);
    }
}