package edu.school21.game;

import edu.school21.chase.Chase;
import edu.school21.chase.ChaseType;
import edu.school21.chase.MovementDirection;
import edu.school21.chase.heplers.Pair;
import edu.school21.game.entities.*;
import edu.school21.game.exceptions.GameGenerateException;
import edu.school21.game.helpers.ConsoleHelper;
import edu.school21.game.printer.GamePrinter;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class Game {
    private final GameField gameField;
    private final GamePrinter printer;
    private final Chase chase;

    public void start() {
        Scanner in = new Scanner(System.in);
        String line;
        render();
        while (!isEnd() && !(line = in.nextLine()).equals("9") ) {
            MovementDirection md = MovementDirection.STAND;
            switch (line.toLowerCase()) {
                case "w":
                    md = MovementDirection.UP;
                    break;
                case "a":
                    md = MovementDirection.LEFT;
                    break;
                case "s":
                    md = MovementDirection.DOWN;
                    break;
                case "d":
                    md = MovementDirection.RIGHT;
                    break;
            }
            boolean isMove = heroMove(md);
            if (isMove) {
                for (Enemy enemy : gameField.getEnemies()) {
                    enemyMove(enemy);
                }
            }
            ConsoleHelper.clear();
            render();
        }
        in.close();
    }

    public Game(int enemiesCount, int wallsCount, int sizeX,int sizeY,  String SettingsFilePath) throws IOException, GameGenerateException {
        gameField = new GameField(enemiesCount, wallsCount, sizeX, sizeY);
        printer = new GamePrinter(SettingsFilePath);
        chase = new Chase(GameEntitiesTypes.VOID.getValue());
    }

    public Game(Integer[][] gameField, String SettingsFilePath) throws IOException {
        this.gameField = new GameField(gameField);
        printer = new GamePrinter(SettingsFilePath);
        chase = new Chase(GameEntitiesTypes.VOID.getValue());
    }

    public void render() {
        printer.printField(gameField);
    }

    public boolean heroMove(MovementDirection direction) {
        return gameField.heroMove(direction);
    }

    public void enemyMove(Enemy enemy) {
        Pair<Integer, Integer> heroPos = gameField.getHeroPosition();
        Pair<Integer, Integer> outputPos = gameField.getOutputPosition();
        Pair<Integer, Integer> enemyPos = enemy.getPosition();
        ChaseType type = enemy.getChaseType();
        MovementDirection md = chase.move(gameField.getField(), enemyPos, heroPos, outputPos, type);
        gameField.enemyMove(enemy, md);
    }

    public boolean isEnd() {
        Pair<Integer, Integer> heroPos = gameField.getHeroPosition();
        Pair<Integer, Integer> OutputPos = gameField.getOutputPosition();
        return heroPos.equals(OutputPos);
    }
}
