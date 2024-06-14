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
                                  Pair<Integer, Integer> output,
                                  ChaseType type) {
        if (!isValid(gameField, enemyPos) || !isValid(gameField, playerPos) || !isValid(gameField, output)) {
            return MovementDirection.STAND;
        }

        if (type == ChaseType.HYBRID) {
            type = ChaseType.getRandom();
        }
        MovementDirection moveDirection = MovementDirection.STAND;
        switch (type) {
            case HUNTER:
                moveDirection = hunterChase(gameField, enemyPos, playerPos, output);
                break;
            case SCOUT:
                break;
            case SECURITY:
                break;
            case STALKER:
                break;
        }

        return moveDirection;
    }

    private MovementDirection hunterChase(Integer[][] gameField,
                                          Pair<Integer, Integer> enemyPos,
                                          Pair<Integer, Integer> playerPos,
                                          Pair<Integer, Integer> output) {
        MovementDirection result = tryMoveTo(enemyPos, playerPos);
        if (result == MovementDirection.STAND) {
            List<Pair<Integer, Integer>> playerPath = buildPath(gameField, playerPos, output);
            if (playerPath != null) {
                for (int i = 0; i < playerPath.size(); i++) {
                    List<Pair<Integer, Integer>> routeToPlayerPath = buildPath(gameField, enemyPos, playerPath.get(i));
                    if (routeToPlayerPath != null && routeToPlayerPath.size() <= i + 1){
                        result = tryMoveTo(enemyPos, routeToPlayerPath.getFirst());
                        break;
                    }
                }
                if (result == MovementDirection.STAND) {
                }
            }
        }
        return result;
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
        boolean isFound = searchInDepth(gameFieldCopy, start, end);
        if (!isFound) {
            return null;
        }
        return restorePath(gameFieldCopy, start, end);
    }


    private boolean searchInDepth(Integer[][] gameFieldCopy,
                                  Pair<Integer, Integer> start,
                                  Pair<Integer, Integer> end) {
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

    private boolean isValid(Integer[][] gameField, Pair<Integer, Integer> pos) {
        return pos.getFirst() < gameField.length &&
                pos.getFirst() >= 0 &&
                pos.getSecond() < gameField[0].length &&
                pos.getSecond() >= 0;
    }
}
