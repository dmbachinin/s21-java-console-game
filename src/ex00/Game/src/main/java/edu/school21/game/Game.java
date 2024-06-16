package edu.school21.game;

import edu.school21.chase.Chase;
import edu.school21.chase.ChaseType;
import edu.school21.chase.MovementDirection;
import edu.school21.chase.heplers.Pair;
import edu.school21.game.entities.*;
import edu.school21.game.exceptions.GameGenerateException;
import edu.school21.game.exceptions.IllegalParametersException;
import edu.school21.game.helpers.ConsoleHelper;
import edu.school21.game.printer.GamePrinter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {
    private final GameField gameField;
    private final GamePrinter printer;
    private final Chase chase;

    public void start(InputStream in) throws GameGenerateException {
        Scanner input = new Scanner(in);
        String line;
        render();
        printer.printControl();
        while (!(line = input.nextLine()).isEmpty() ) {
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
                case "r":
                    gameField.regenerate();
                    break;
            }
            boolean isMove = heroMove(md);
            boolean isCatch = false;
            if (isMove && !isEnd()) {
                for (Enemy enemy : gameField.getEnemies()) {
                    isCatch = enemyMove(enemy);
                    if (isCatch) {
                        break;
                    }
                }
            }
            ConsoleHelper.clear();
            render();
            if (isEnd() || isCatch || line.equals("9")) {
                if (isEnd()) {
                    printer.printWin();
                } else {
                    printer.printLose();
                }
                if (input.nextLine().equals("r")) {
                    gameField.regenerate();
                    render();
                    printer.printControl();
                } else {
                    break;
                }
            } else {
                printer.printControl();
            }

        }
        input.close();
    }

    public Game(int enemiesCount, int wallsCount, int sizeX,int sizeY,  String SettingsFilePath) throws IOException, IllegalParametersException {
        gameField = new GameField(enemiesCount, wallsCount, sizeX, sizeY);
        printer = new GamePrinter(SettingsFilePath);
        chase = new Chase(GameEntitiesTypes.VOID.getValue());
    }

    public Game(Integer[][] gameField, String SettingsFilePath) throws IOException, GameGenerateException {
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

    public boolean enemyMove(Enemy enemy) {
        Pair<Integer, Integer> heroPos = gameField.getHeroPosition();
        Pair<Integer, Integer> outputPos = gameField.getOutputPosition();
        Pair<Integer, Integer> enemyPos = enemy.getPosition();
        ChaseType type = enemy.getChaseType();
        MovementDirection md = chase.move(gameField.getField(), enemyPos, heroPos, outputPos, type);
        return gameField.enemyMove(enemy, md);
    }

    public boolean isEnd() {
        Pair<Integer, Integer> heroPos = gameField.getHeroPosition();
        Pair<Integer, Integer> OutputPos = gameField.getOutputPosition();
        return heroPos.equals(OutputPos);
    }
}
