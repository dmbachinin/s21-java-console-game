package edu.school21.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private String enemyChar = "X";
    private String playerChar = "o";
    private String wallChar = "#";
    private String goalChar = "O";
    private String emptyChar = " ";
    private String enemyColor = "RED";
    private String playerColor = "GREEN";
    private String wallColor = "MAGENTA";
    private String goalColor = "BLUE";
    private String emptyColor = "YELLOW";

    public Settings(String filePath) {
        Properties properties = new Properties();
        try (InputStream input = Settings.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + filePath);
                return;
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        enemyChar = properties.getProperty("enemy.char", enemyChar);
        playerChar = properties.getProperty("player.char", playerChar);
        wallChar = properties.getProperty("wall.char", wallChar);
        goalChar = properties.getProperty("goal.char", goalChar);
        emptyChar = properties.getProperty("empty.char", emptyChar);
        if (emptyChar.isEmpty()) {
            emptyChar = " ";
        }
        enemyColor = properties.getProperty("enemy.color", enemyColor);
        playerColor = properties.getProperty("player.color", playerColor);
        wallColor = properties.getProperty("wall.color", wallColor);
        goalColor = properties.getProperty("goal.color", goalColor);
        emptyColor = properties.getProperty("empty.color", emptyColor);
    }

    public String getEnemyChar () {
        return enemyChar;
    }
    public String getPlayerChar () {
        return playerChar;
    }
    public String getWallChar () {
        return wallChar;
    }
    public String getGoalChar () {
        return goalChar;
    }
    public String getEmptyChar () {
        return emptyChar;
    }
    public String getEnemyColor () {
        return enemyColor;
    }
    public String getPlayerColor () {
        return playerColor;
    }
    public String getWallColor () {
        return wallColor;
    }
    public String getGoalColor () {
        return goalColor;
    }
    public String getEmptyColor () {
        return emptyColor;
    }
}
