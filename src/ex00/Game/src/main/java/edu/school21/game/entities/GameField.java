package edu.school21.game.entities;

import edu.school21.game.heplers.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    private final Random rnd;
    private final Integer[][] field;
    private final int size;
    private final int enemiesCount;
    private final int wallsCount;
    private final int outputsCount;
    private final int spaceAroundOutput;
    private final int spaceAroundHero;

    private List<Enemy> enemies;
    private List<Pair<Integer, Integer>> outputs;
    private Hero hero;


    public GameField(int enemiesCount, int wallsCount, int size) {
        rnd = new Random();
        field = new Integer[size][size];
        for (int x = 0; x < field.length; x++){
            for (int y = 0; y < field[0].length; y++){
                field[x][y] = GameEntitiesTypes.VOID.getValue();
            }
        }
        this.enemiesCount = enemiesCount;
        this.wallsCount = wallsCount;
        this.size = size;

        outputsCount = 1;
        spaceAroundOutput = 3;
        spaceAroundHero = 2;

        enemies = new ArrayList<>();
        outputs = new ArrayList<>();
        boolean isGenerate = generate();
        if (!isGenerate) {
            System.err.println("ОШИБКА ГЕНЕРАЦИИ!!");
        }
    }

    private boolean generate() {
        if (enemiesCount + wallsCount + outputsCount + 1 >= size * size) {
            return false;
        }

        return generateOutputs() && generateHero() && generateEnemies() && generateWalls();
    }

    private boolean generateOutputs() {
        for (int i = 0; i < outputsCount; i++) {
            Pair<Integer, Integer> outputPos = getRandomPos(GameEntitiesTypes.OUTPUT);
            if (outputPos == null) {
                return false;
            }
            setEntity(outputPos, GameEntitiesTypes.OUTPUT);
            outputs.add(outputPos);
        }
        return true;
    }

    private boolean generateHero() {
        Pair<Integer, Integer> heroPos = getRandomPos(GameEntitiesTypes.HERO);
        if (heroPos == null) {
            return false;
        }
        setEntity(heroPos, GameEntitiesTypes.HERO);
        hero = new Hero(heroPos);
        return true;
    }

    private boolean generateEnemies() {
        for (int i = 0; i < enemiesCount; i++) {
            Pair<Integer, Integer> enemyPos = getRandomPos(GameEntitiesTypes.ENEMY);
            if (enemyPos == null) {
                return false;
            }
            setEntity(enemyPos, GameEntitiesTypes.ENEMY);
            Enemy enemy = new Enemy(enemyPos);
            enemies.add(enemy);
        }
        return true;
    }

    private boolean generateWalls() {
        for (int i = 0; i < wallsCount; i++) {
            Pair<Integer, Integer> wallPos = getRandomPos(GameEntitiesTypes.ENEMY);
            if (wallPos == null) {
                return false;
            }
            setEntity(wallPos, GameEntitiesTypes.WALL);
        }
        return true;
    }

    private Pair<Integer, Integer> getRandomPos(GameEntitiesTypes entity) {
        List<Pair<Integer, Integer>> possiblePos = getPossiblePos(entity);
        if (possiblePos.isEmpty()) {
            return null;
        }
        return possiblePos.get(rnd.nextInt(possiblePos.size()));
    }

    private List<Pair<Integer, Integer>> getPossiblePos(GameEntitiesTypes entity) {
        List<Pair<Integer, Integer>> possiblePos = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (getSellType(x, y) == GameEntitiesTypes.VOID) {
                    if (entity == GameEntitiesTypes.WALL) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.OUTPUT && getSellType(x, y) == GameEntitiesTypes.VOID) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.HERO && checkNeighbors(x, y, spaceAroundOutput)) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.ENEMY && checkNeighbors(x, y, spaceAroundHero)) {
                        possiblePos.add(new Pair<>(x, y));
                    }
                }
            }
        }
        return possiblePos;
    }

    private boolean checkNeighbors(int xPos, int yPos, int radius) {
        boolean result = true;
        external:
        for (int x = xPos - radius; x < xPos + radius; x++) {
            for (int y = yPos - radius; y < yPos + radius; y++) {
                if (x < size && x >= 0 && y < size && y >= 0) {
                    if (getSellType(x, y) == GameEntitiesTypes.HERO ||
                            getSellType(x, y) == GameEntitiesTypes.OUTPUT) {
                        result = false;
                        break external;
                    }
                }
            }
        }
        return result;
    }

    private void setEntity(Pair<Integer, Integer> pos, GameEntitiesTypes entity) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        field[x][y] = entity.getValue();
    }

    public GameEntitiesTypes getSellType(int x, int y) {
        if (x < 0 || x >= size && y < 0 || y >= size) {
            return null;
        }
        return GameEntitiesTypes.values()[field[x][y]];
    }

    public Integer[][] getField() {
        return field;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Pair<Integer, Integer>> getOutputs() {
        return outputs;
    }

    public int getSize() {
        return size;
    }
}
