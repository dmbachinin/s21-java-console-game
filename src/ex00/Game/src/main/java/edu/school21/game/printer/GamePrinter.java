package edu.school21.game.printer;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import edu.school21.chase.heplers.Pair;
import edu.school21.game.Settings;
import edu.school21.game.entities.Enemy;
import edu.school21.game.entities.GameField;

import java.io.IOException;
import java.util.List;

public class GamePrinter {
    private Settings settings;

    public GamePrinter(String SettingsFilePath){
        settings = new Settings(SettingsFilePath);
    }

    public void printWin() {
        System.out.println("Вы победили!");
        printMainControl();
    }

    public void printLose() {
        System.out.println("Вы проиграли :(");
        printMainControl();
    }

    public void printControl() {
        System.out.println("Управление:");
        System.out.println(" w ");
        System.out.println("asd - Перемещение");
        System.out.println("f - Пропустить ход");
        System.out.println("r - Перезапустить игру");
        System.out.println("9 - Сдаться");
    }

    public void printDevConfirmEnemyStep(Enemy enemy) {
        System.out.println("Противник: " + enemy.getChaseType());
        System.out.println("8 - Подтвердить ход противника");
        System.out.println("с - Отменить ход");
    }

    private void printMainControl() {
        System.out.println("r - Перезапустить игру");
        System.out.println("Any Key - Выйти");
    }

    public void printField(GameField gameField) {
        int sizeX = gameField.getSizeX();
        int sizeY = gameField.getSizeY();
        ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();
        ;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Ansi.BColor bColor = Ansi.BColor.NONE;
                String sellChar = "";
                switch (gameField.getSellType(x, y)) {
                    case VOID:
                        bColor = Ansi.BColor.valueOf(settings.getEmptyColor());
                        sellChar = settings.getEmptyChar();
                        break;
                    case ENEMY:
                        bColor = Ansi.BColor.valueOf(settings.getEnemyColor());
                        sellChar = settings.getEnemyChar();
                        break;
                    case HERO:
                        bColor = Ansi.BColor.valueOf(settings.getPlayerColor());
                        sellChar = settings.getPlayerChar();
                        break;
                    case OUTPUT:
                        bColor = Ansi.BColor.valueOf(settings.getGoalColor());
                        sellChar = settings.getGoalChar();
                        break;
                    case WALL:
                        bColor = Ansi.BColor.valueOf(settings.getWallColor());
                        sellChar = settings.getWallChar();
                        break;
                }
                printer.print(sellChar, Ansi.Attribute.NONE,
                        Ansi.FColor.NONE, bColor);
            }
            printer.print("\n", Ansi.Attribute.NONE,
                    Ansi.FColor.NONE, Ansi.BColor.NONE);
        }
    }

    public void printDevField(GameField gameField, List<Pair<Integer, Integer>> highlightsPos) {
        int sizeX = gameField.getSizeX();
        int sizeY = gameField.getSizeY();
        ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Ansi.BColor bColor = Ansi.BColor.NONE;
                String sellChar = "";
                switch (gameField.getSellType(x, y)) {
                    case VOID:
                        bColor = Ansi.BColor.valueOf(settings.getEmptyColor());
                        sellChar = settings.getEmptyChar();
                        break;
                    case ENEMY:
                        bColor = Ansi.BColor.valueOf(settings.getEnemyColor());
                        sellChar = settings.getEnemyChar();
                        break;
                    case HERO:
                        bColor = Ansi.BColor.valueOf(settings.getPlayerColor());
                        sellChar = settings.getPlayerChar();
                        break;
                    case OUTPUT:
                        bColor = Ansi.BColor.valueOf(settings.getGoalColor());
                        sellChar = settings.getGoalChar();
                        break;
                    case WALL:
                        bColor = Ansi.BColor.valueOf(settings.getWallColor());
                        sellChar = settings.getWallChar();
                        break;
                }
                if (isHighlightPos(new Pair<>(x, y), highlightsPos)) {
                    bColor = Ansi.BColor.YELLOW;
                }
                printer.print(sellChar, Ansi.Attribute.NONE,
                        Ansi.FColor.NONE, bColor);
            }
            printer.print("\n", Ansi.Attribute.NONE,
                    Ansi.FColor.NONE, Ansi.BColor.NONE);
        }
    }
    private boolean isHighlightPos(Pair<Integer, Integer> pos, List<Pair<Integer, Integer>> highlightPos) {
        for (Pair<Integer, Integer> p : highlightPos) {
            if (p.equals(pos)) {
                return true;
            }
        }
        return false;
    }
}
