package edu.school21.game.entities;

import edu.school21.chase.ChaseType;
import edu.school21.chase.heplers.Pair;

public class Enemy {
    private Pair<Integer, Integer> position;
    private ChaseType type;

    public Enemy(Pair<Integer, Integer> startPos) {
        type = ChaseType.getRandom();
        position = startPos;
    }

    public Enemy(Pair<Integer, Integer> startPos, ChaseType type) {
        this.type = type;
        position = startPos;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public ChaseType getChaseType() {
        return type;
    }
}
