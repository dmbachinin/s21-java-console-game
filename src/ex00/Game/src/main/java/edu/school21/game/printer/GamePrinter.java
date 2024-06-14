package edu.school21.game.printer;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import edu.school21.game.Settings;
import edu.school21.game.entities.GameField;

import java.io.IOException;

public class GamePrinter {
    private Settings settings;
    public GamePrinter(String SettingsFilePath) throws IOException {
        settings = new Settings(SettingsFilePath);
    }

    public void printField(GameField gameField) {
        int sizeX = gameField.getSizeX();
        int sizeY = gameField.getSizeY();
        ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();;
        for (int x = 0; x < sizeX; x++){
            for (int y = 0; y < sizeY; y++){
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
}
