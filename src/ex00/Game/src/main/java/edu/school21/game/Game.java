package edu.school21.game;

import edu.school21.game.entities.GameField;
import edu.school21.game.printer.GamePrinter;

import java.io.IOException;

public class Game {
    private final GameField gameField;
    private final GamePrinter printer;

    public Game(int enemiesCount, int wallsCount, int size, String SettingsFilePath) throws IOException {
        gameField = new GameField(enemiesCount, wallsCount, size);
        printer = new GamePrinter(SettingsFilePath);
    }

    public void render() {
        printer.printField(gameField);
    }
}
