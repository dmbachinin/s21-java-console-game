package edu.school21.chase;

import java.util.Random;

public class Chase {
    private static Random rnd = new Random();
    public static MovementDirection move(Integer[][] gameField, int EnemyXPos, int EnemyYPos, int PlayerXPos, int PlayerYPos, ChaseType type){
        if (isValid(gameField, EnemyXPos, EnemyYPos, PlayerXPos, PlayerYPos)) {
            return MovementDirection.STAND;
        }
        if (type == ChaseType.HYBRID) {
            type = ChaseType.getRandom();
        }
        return MovementDirection.STAND;
    }

    private static boolean isValid(Integer[][] gameField, int EnemyXPos, int EnemyYPos, int PlayerXPos, int PlayerYPos) {
        boolean valid = true;
        int width = gameField.length;
        int height = gameField[0].length;
        if (EnemyXPos >= height || PlayerXPos >= height) {
            valid = false;
        } else if (EnemyYPos >= width || PlayerYPos >= width) {
            valid = false;
        }
        return valid;
    }
}
