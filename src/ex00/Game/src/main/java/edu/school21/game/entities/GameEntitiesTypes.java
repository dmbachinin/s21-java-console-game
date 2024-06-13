package edu.school21.game.entities;

public enum GameEntitiesTypes {
    VOID(0), ENEMY(1), HERO(2), OUTPUT(3), WALL(4);
    private final int val;

    GameEntitiesTypes(int i) {
        val = i;
    }

    public int getValue() {
        return val;
    }
}
