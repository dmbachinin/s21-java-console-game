package edu.school21.chase;

public enum MovementDirection {
    RIGHT(0, 1),
    LEFT(0, -1),
    UP(-1, 0),
    DOWN(1, 0),
    STAND(0, 0);
    private final int changeX;
    private final int changeY;
    MovementDirection(int changeX, int changeY) {
        this.changeX = changeX;
        this.changeY = changeY;
    }

    public int getChangeX() {
        return changeX;
    }

    public int getChangeY() {
        return changeY;
    }
}