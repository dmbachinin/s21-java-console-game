package edu.school21.game.entities;

import edu.school21.chase.Chase;
import edu.school21.chase.MovementDirection;
import edu.school21.chase.heplers.Pair;
import edu.school21.game.exceptions.GameGenerateException;
import edu.school21.game.exceptions.IllegalParametersException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameField {
    private Random rnd = new Random();
    private Integer[][] field;
    private int sizeX;
    private int sizeY;
    private int enemiesCount;
    private int wallsCount;
    private final int SPACE_AROUND_OUTPUT = 3;
    private final int SPACE_AROUND_HERO = 2;
    private final int GENERATION_ATTEMPTS_COUNT = 10000;

    private List<Enemy> enemies;
    private Pair<Integer, Integer> output;
    private Pair<Integer, Integer> hero;


    public GameField(int enemiesCount, int wallsCount, int sizeX, int sizeY) throws IllegalParametersException {
        this.enemiesCount = enemiesCount;
        this.wallsCount = wallsCount;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.field = new Integer[sizeX][sizeY];
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[0].length; y++) {
                field[x][y] = GameEntitiesTypes.VOID.getValue();
            }
        }

        this.enemies = new ArrayList<>();
        boolean isGenerate = generate();
        int genCount = 1;
        while (!isGenerate && genCount != GENERATION_ATTEMPTS_COUNT) {
            isGenerate = generate();
            genCount++;
        }
        if (!isGenerate) {
            throw new IllegalParametersException("Не удалось сгененировать игровое поле по заданным параметрам");
        }
    }
    public void regenerate() throws GameGenerateException {
        this.field = new Integer[sizeX][sizeY];
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[0].length; y++) {
                field[x][y] = GameEntitiesTypes.VOID.getValue();
            }
        }
        this.hero = null;
        this.enemies = new ArrayList<>();
        this.output = null;
        boolean isGenerate = generate();
        int genCount = 1;
        while (!isGenerate && genCount != GENERATION_ATTEMPTS_COUNT) {
            isGenerate = generate();
            genCount++;
        }
        if (!isGenerate) {
            throw new GameGenerateException("Не удалось сгененировать игровое поле по заданным параметрам");
        }
    }
    public GameField(Integer[][] gameField) throws GameGenerateException {
        enemies = new ArrayList<>();
        boolean isParsed = parseGameField(gameField);
        if (!isParsed) {
            throw new GameGenerateException("Не удалось сгененировать игровое поле по начальной матрице");
        }
    }

    private boolean parseGameField(Integer[][] gameField) {
        this.enemiesCount = 0;
        this.wallsCount = 0;
        this.sizeX = gameField.length;
        this.sizeY = gameField[0].length;
        this.field = new Integer[sizeX][sizeY];
        this.hero = null;
        this.enemies = new ArrayList<>();
        this.output = null;

        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[0].length; y++) {
                Integer cellValue = gameField[x][y];
                GameEntitiesTypes type = GameEntitiesTypes.getTypeForVal(cellValue);
                field[x][y] = type != null ? gameField[x][y] : GameEntitiesTypes.VOID.getValue();
                if (type == GameEntitiesTypes.ENEMY) {
                    enemiesCount++;
                    enemies.add(new Enemy(new Pair<>(x, y)));
                } else if (type == GameEntitiesTypes.HERO) {
                    if (hero != null) {
                        return false;
                    }
                    hero = new Pair<>(x, y);
                } else if (type == GameEntitiesTypes.OUTPUT) {
                    if (output != null) {
                        return false;
                    }
                    output = new Pair<>(x, y);
                } else if (type == GameEntitiesTypes.WALL) {
                    wallsCount++;
                }
            }
        }
        return output != null && hero != null;
    }

    private boolean generate() {
        if (enemiesCount + wallsCount + 2 >= sizeX * sizeY) {
            return false;
        }
        boolean isGenerate = generateOutput() && generateHero() && generateEnemies() && generateWalls();
        boolean isPassable = false;
        if (isGenerate) {
            Integer[][] gameFieldCopy = Arrays.stream(field).map(Integer[]::clone).toArray(Integer[][]::new);
            gameFieldCopy[hero.getFirst()][hero.getSecond()] = 100;
            isPassable = Chase.BFS(gameFieldCopy, hero, output, GameEntitiesTypes.VOID.getValue());
        }

        return isGenerate && isPassable;
    }

    private boolean generateOutput() {
        Pair<Integer, Integer> outputPos = getRandomPos(GameEntitiesTypes.OUTPUT);
        if (outputPos == null) {
            return false;
        }
        setEntity(outputPos, GameEntitiesTypes.OUTPUT);
        output = outputPos;
        return true;
    }

    private boolean generateHero() {
        Pair<Integer, Integer> heroPos = getRandomPos(GameEntitiesTypes.HERO);
        if (heroPos == null) {
            return false;
        }
        setEntity(heroPos, GameEntitiesTypes.HERO);
        hero = heroPos;
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
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (getSellType(x, y) == GameEntitiesTypes.VOID) {
                    if (entity == GameEntitiesTypes.WALL) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.OUTPUT && getSellType(x, y) == GameEntitiesTypes.VOID) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.HERO && checkNeighbors(x, y, SPACE_AROUND_OUTPUT)) {
                        possiblePos.add(new Pair<>(x, y));
                    } else if (entity == GameEntitiesTypes.ENEMY && checkNeighbors(x, y, SPACE_AROUND_HERO)) {
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
                if (x < sizeX && x >= 0 && y < sizeY && y >= 0) {
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
        return isValidPosition(x, y) ? GameEntitiesTypes.getTypeForVal(field[x][y]) : null;
    }

    public Integer[][] getField() {
        return field;
    }

    public Pair<Integer, Integer> getHeroPosition() {
        return hero;
    }

    public boolean heroMove(MovementDirection direction) {
        return move(hero, GameEntitiesTypes.HERO, direction);
    }

    public boolean enemyMove(Enemy enemy, MovementDirection direction) {
        move(enemy.getPosition(), GameEntitiesTypes.ENEMY, direction);
        return enemy.getPosition().equals(hero);
    }

    private boolean move(Pair<Integer, Integer> position, GameEntitiesTypes type, MovementDirection direction) {
        int nextX = position.getFirst() + direction.getChangeX();
        int nextY = position.getSecond() + direction.getChangeY();
        boolean isMove = isValidPosition(nextX, nextY) && getSellType(nextX, nextY) != GameEntitiesTypes.WALL &&
                getSellType(nextX, nextY) != GameEntitiesTypes.ENEMY;
        if (isMove) {
            setEntity(position, GameEntitiesTypes.VOID);
            position.setFirst(nextX);
            position.setSecond(nextY);
            setEntity(position, type);
        }
        return isMove;
    }

    private boolean isValidPosition(int x, int y) {
        return !(x < 0 || x >= sizeX || y < 0 || y >= sizeY);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Pair<Integer, Integer> getOutputPosition() {
        return output;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
