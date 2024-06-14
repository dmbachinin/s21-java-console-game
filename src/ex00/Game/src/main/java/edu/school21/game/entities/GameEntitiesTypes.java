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

    public static GameEntitiesTypes getTypeForVal(int val) {
        GameEntitiesTypes[] gets = GameEntitiesTypes.values();
        for (GameEntitiesTypes get : gets) {
            if (get.getValue() == val) {
                return get;
            }
        }
        return null;
    }
}
