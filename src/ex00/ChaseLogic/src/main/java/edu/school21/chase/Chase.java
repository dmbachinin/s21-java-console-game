package edu.school21.chase;

import edu.school21.chase.heplers.Pair;

import java.util.*;

public class Chase {
    private final Integer voidValue;
    private final Integer startSearchValue;

    public Chase(Integer voidValue) {
        this.voidValue = voidValue;
        startSearchValue = 100;
    }

    public MovementDirection move(Integer[][] gameField,
                                  Pair<Integer, Integer> enemyPos,
                                  Pair<Integer, Integer> playerPos,
                                  Pair<Integer, Integer> outputPos,
                                  ChaseType type) {
        if (!isValid(gameField, enemyPos) || !isValid(gameField, playerPos) || !isValid(gameField, outputPos)) {
            return MovementDirection.STAND;
        }

        if (type == ChaseType.HYBRID) {
            type = ChaseType.getRandom();
        }
        MovementDirection moveDirection = MovementDirection.STAND;
        switch (type) {
            case HUNTER:
                moveDirection = hunterChase(gameField, enemyPos, playerPos, outputPos);
                break;
            case SCOUT:
                moveDirection = scoutChase(gameField, enemyPos, playerPos, outputPos);
                break;
            case SECURITY:
                moveDirection = securityChase(gameField, enemyPos, playerPos, outputPos);
                break;
            case STALKER:
                moveDirection = stalkerChase(gameField, enemyPos, playerPos);
                break;
        }

        return moveDirection;
    }

    private MovementDirection hunterChase(Integer[][] gameField,
                                          Pair<Integer, Integer> enemyPos,
                                          Pair<Integer, Integer> playerPos,
                                          Pair<Integer, Integer> outputPos) {
        MovementDirection result = tryMoveTo(enemyPos, playerPos);
        if (result == MovementDirection.STAND) {
            List<Pair<Integer, Integer>> playerPath = buildPath(gameField, playerPos, outputPos);
            if (playerPath != null && !playerPath.isEmpty()) {
                for (int i = 0; i < playerPath.size(); i++) {
                    List<Pair<Integer, Integer>> routeToPlayerPath = buildPath(gameField, enemyPos, playerPath.get(i));
                    if (routeToPlayerPath != null && routeToPlayerPath.size() <= i + 1) {
                        result = tryMoveTo(enemyPos, routeToPlayerPath.getFirst());
                        break;
                    }
                }
                if (result == MovementDirection.STAND) {
                    result = move(gameField, enemyPos, playerPos, outputPos, ChaseType.STALKER);
                }
            }
        }
        return result;
    }

    private MovementDirection securityChase(Integer[][] gameField,
                                            Pair<Integer, Integer> enemyPos,
                                            Pair<Integer, Integer> playerPos,
                                            Pair<Integer, Integer> outputPos) {
        MovementDirection result = tryMoveTo(enemyPos, playerPos);
        if (result == MovementDirection.STAND) {
            result = tryMoveTo(enemyPos, outputPos);
            if (result == MovementDirection.STAND) {
                List<Pair<Integer, Integer>> outputPath = buildPath(gameField, enemyPos, outputPos);
                if (outputPath != null && !outputPath.isEmpty()) {
                    result = tryMoveTo(enemyPos, outputPath.getFirst());
                }
            } else {
                result = move(gameField, enemyPos, playerPos, outputPos, ChaseType.STALKER);
            }
        }
        return result;
    }

    private MovementDirection stalkerChase(Integer[][] gameField,
                                           Pair<Integer, Integer> enemyPos,
                                           Pair<Integer, Integer> playerPos) {
        MovementDirection result = tryMoveTo(enemyPos, playerPos);
        if (result == MovementDirection.STAND) {
            List<Pair<Integer, Integer>> pathToPlayer = buildPath(gameField, enemyPos, playerPos);
            if (pathToPlayer != null  && !pathToPlayer.isEmpty()) {
                result = tryMoveTo(enemyPos, pathToPlayer.getFirst());
            }
        }
        return result;
    }

    private MovementDirection scoutChase(Integer[][] gameField,
                                         Pair<Integer, Integer> enemyPos,
                                         Pair<Integer, Integer> playerPos,
                                         Pair<Integer, Integer> outputPos) {
        MovementDirection result = tryMoveTo(enemyPos, playerPos);
        if (result == MovementDirection.STAND) {
            List<Pair<Integer, Integer>> playerPath = buildPath(gameField, playerPos, outputPos);
            if (playerPath != null && !playerPath.isEmpty()) {
                MovementDirection playerNextMove = tryMoveTo(playerPos, playerPath.getFirst());
                Pair<Integer, Integer> targetPos = getNextPosition(playerPos, MovementDirection.getOpposite(playerNextMove));
                List<Pair<Integer, Integer>> enemyPath = buildPath(gameField, enemyPos, targetPos);
                if (enemyPath != null && !enemyPath.isEmpty()) {
                    result = tryMoveTo(enemyPos, enemyPath.getFirst());
                }
            }
        }
        return result;
    }

    private Pair<Integer, Integer> getNextPosition(Pair<Integer, Integer> pos, MovementDirection movementDirection) {
        return new Pair<>(pos.getFirst() + movementDirection.getChangeX(), pos.getSecond() + movementDirection.getChangeY());
    }

    private MovementDirection tryMoveTo(Pair<Integer, Integer> start,
                                        Pair<Integer, Integer> end) {
        MovementDirection[] mds = MovementDirection.values();
        MovementDirection result = MovementDirection.STAND;
        for (MovementDirection md : mds) {
            Integer nextEnemyPosX = start.getFirst() + md.getChangeX();
            Integer nextEnemyPosY = start.getSecond() + md.getChangeY();
            if (end.getFirst().equals(nextEnemyPosX) &&
                    end.getSecond().equals(nextEnemyPosY)) {
                result = md;
                break;
            }
        }
        return result;
    }

    private List<Pair<Integer, Integer>> buildPath(Integer[][] gameField,
                                                   Pair<Integer, Integer> start,
                                                   Pair<Integer, Integer> end) {
        Integer[][] gameFieldCopy = Arrays.stream(gameField).map(Integer[]::clone).toArray(Integer[][]::new);
        gameFieldCopy[start.getFirst()][start.getSecond()] = startSearchValue;
        boolean isFound = BFS(gameFieldCopy, start, end, voidValue);
        if (!isFound) {
            return null;
        }
        return restorePath(gameFieldCopy, start, end);
    }


    public static boolean BFS(Integer[][] gameFieldCopy,
                                  Pair<Integer, Integer> start,
                                  Pair<Integer, Integer> end, Integer voidValue) {
        MovementDirection[] mds = MovementDirection.values();
        Queue<Pair<Integer, Integer>> stack = new LinkedList<>();
        stack.add(start);
        boolean isFound = false;
        main:
        while (!stack.isEmpty()) {
            Pair<Integer, Integer> currentPos = stack.remove();
            for (MovementDirection md : mds) {
                Integer currentX = currentPos.getFirst();
                Integer currentY = currentPos.getSecond();
                Integer nextX = currentX + md.getChangeX();
                Integer nextY = currentY + md.getChangeY();

                if (isValid(gameFieldCopy, new Pair<>(nextX, nextY)) &&
                        nextX.equals(end.getFirst()) &&
                        nextY.equals(end.getSecond())) {
                    gameFieldCopy[nextX][nextY] = gameFieldCopy[currentX][currentY] + 1;
                    isFound = true;
                    break main;
                } else if (isValid(gameFieldCopy, new Pair<>(nextX, nextY)) &&
                        gameFieldCopy[nextX][nextY].equals(voidValue)) {
                    stack.add(new Pair<>(nextX, nextY));
                    gameFieldCopy[nextX][nextY] = gameFieldCopy[currentX][currentY] + 1;
                }
            }
        }
        return isFound;
    }

    private List<Pair<Integer, Integer>> restorePath(Integer[][] gameFieldCopy,
                                                     Pair<Integer, Integer> start,
                                                     Pair<Integer, Integer> end) {
        LinkedList<Pair<Integer, Integer>> result = new LinkedList<>();
        MovementDirection[] mds = MovementDirection.values();
        Integer currentX = end.getFirst();
        Integer currentY = end.getSecond();
        while (!currentX.equals(start.getFirst()) || !currentY.equals(start.getSecond())) {
            for (MovementDirection md : mds) {
                Integer nextX = currentX + md.getChangeX();
                Integer nextY = currentY + md.getChangeY();
                if (isValid(gameFieldCopy, new Pair<>(nextX, nextY)) &&
                        gameFieldCopy[nextX][nextY].equals(gameFieldCopy[currentX][currentY] - 1)) {
                    result.addFirst(new Pair<>(currentX, currentY));
                    currentX = nextX;
                    currentY = nextY;
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isValid(Integer[][] gameField, Pair<Integer, Integer> pos) {
        return pos.getFirst() < gameField.length &&
                pos.getFirst() >= 0 &&
                pos.getSecond() < gameField[0].length &&
                pos.getSecond() >= 0;
    }
}
