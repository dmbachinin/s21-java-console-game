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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final GameField gameField;
    private final GamePrinter printer;
    private GamePrinter devPrinter;
    private final Chase chase;
    private boolean isDev;

    public void start(InputStream in) throws GameGenerateException {
        if (isDev) {
            playInDevMode(in);
        } else {
            playInDefaultMode(in);
        }
    }

    public void playInDefaultMode(InputStream in) throws GameGenerateException {
        Scanner input = new Scanner(in);
        String line;
        render();
        printer.printControl();
        while (!(line = input.nextLine()).isEmpty()) {
            MovementDirection md = userInput(line);
            boolean isCatch = false;
            if (md != MovementDirection.STAND || line.equals("f")) {
                boolean isMove = heroMove(md);
                isCatch = enemiesMove(isMove);
            }
            ConsoleHelper.clear();
            render();
            boolean isGameEnd = gameEnd(isCatch, line, input);
            if (isGameEnd) {
                break;
            }
        }
        input.close();
    }

    private boolean enemiesMove(boolean isHeroMove) {
        boolean isCatch = false;
        if (isHeroMove && !isEnd()) {
            for (Enemy enemy : gameField.getEnemies()) {
                isCatch = enemyMove(enemy);
                if (isCatch) {
                    break;
                }
            }
        }
        return  isCatch;
    }

    private void playInDevMode(InputStream in) throws GameGenerateException {
        Scanner input = new Scanner(in);
        System.out.println("Game start in Dev mode");
        devPrinter.printField(gameField);
        devPrinter.printControl();
        while (true) {
            String line = input.nextLine();
            MovementDirection md = userInput(line);
            boolean isCatch = false;
            if (md != MovementDirection.STAND || line.equals("f")) {
                boolean isMove = heroMove(md);
                isCatch = enemiesDevMove(isMove, input);
            }
            devPrinter.printField(gameField);
            boolean isGameEnd = gameEnd(isCatch, line, input);
            if (isGameEnd) {
                break;
            }
        }
        input.close();
    }
    private MovementDirection userInput(String inputCommand) throws GameGenerateException {
        MovementDirection md = MovementDirection.STAND;
        switch (inputCommand.toLowerCase()) {
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
            case "f":
                md = MovementDirection.STAND;
                break;
            case "r":
                gameField.regenerate();
                break;
        }
        return md;
    }
    private boolean gameEnd(boolean isCatch, String line, Scanner input) throws GameGenerateException {
        boolean result = false;
        GamePrinter printerCurrent = isDev ? devPrinter : printer;
        if (isEnd() || isCatch || line.equals("9")) {
            if (isEnd()) {
                printerCurrent.printWin();
            } else {
                printerCurrent.printLose();
            }
            if (input.nextLine().equals("r")) {
                gameField.regenerate();
                if (!isDev) {
                    ConsoleHelper.clear();
                }
                printerCurrent.printField(gameField);
                printerCurrent.printControl();
            } else {
                result = true;
            }
        } else {
            printerCurrent.printControl();
        }
        return result;
    }

    private boolean enemiesDevMove(boolean isHeroMove, Scanner input) {
        boolean isCatch = false;
        if (isHeroMove && !isEnd()) {
            for (Enemy enemy : gameField.getEnemies()) {
                List <Pair<Integer, Integer>> highlightsPos = new ArrayList<>();
                highlightsPos.add(new Pair<>(enemy.getPosition().getFirst(), enemy.getPosition().getSecond()));
                isCatch = enemyMove(enemy);
                highlightsPos.add(enemy.getPosition());
                devPrinter.printDevField(gameField, highlightsPos);
                devPrinter.printDevConfirmEnemyStep(enemy);
                String line = input.nextLine();
                if (line.equals("c")) {
                    gameField.enemyMove(enemy, MovementDirection.getOpposite(enemy.getLastMove()));
                    isCatch = false;
                }
                if (isCatch) {
                    break;
                }
            }
        }
        return isCatch;
    }

    public Game(int enemiesCount, int wallsCount, int sizeX,int sizeY,  String SettingsFilePath) throws IOException, IllegalParametersException {
        gameField = new GameField(enemiesCount, wallsCount, sizeX, sizeY);
        printer = new GamePrinter(SettingsFilePath);
        chase = new Chase(GameEntitiesTypes.VOID.getValue());
        isDev = false;
    }



    public Game(Integer[][] gameField, String SettingsFilePath) throws IOException, GameGenerateException {
        this.gameField = new GameField(gameField);
        printer = new GamePrinter(SettingsFilePath);
        chase = new Chase(GameEntitiesTypes.VOID.getValue());
        isDev = false;
    }

    public void devModeOn(String SettingsFilePath) throws IOException {
        isDev = true;
        devPrinter = new GamePrinter(SettingsFilePath);
    }

    public void devModeOff() {
        isDev = false;
        devPrinter = null;
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
        enemy.setLastMove(md);
        return gameField.enemyMove(enemy, md);
    }

    public boolean isEnd() {
        Pair<Integer, Integer> heroPos = gameField.getHeroPosition();
        Pair<Integer, Integer> OutputPos = gameField.getOutputPosition();
        return heroPos.equals(OutputPos);
    }
}
