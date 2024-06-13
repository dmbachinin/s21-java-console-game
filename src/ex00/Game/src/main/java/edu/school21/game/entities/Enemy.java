package edu.school21.game.entities;

import edu.school21.chase.ChaseType;
import edu.school21.chase.MovementDirection;
import edu.school21.game.heplers.Pair;

public class Enemy {
    private ChaseType type;
    private int xPos;
    private int yPos;

    public Enemy(Pair<Integer, Integer> pos) {
        type = ChaseType.getRandom();
        xPos = pos.getFirst();
        yPos = pos.getSecond();
    }

    public Enemy(Pair<Integer, Integer> pos, ChaseType type) {
        this.type = type;
        xPos = pos.getFirst();
        yPos = pos.getSecond();
    }

    public Pair<Integer, Integer> getPos() {
        return new Pair<>(xPos, yPos);
    }

    public ChaseType getType() {
        return type;
    }
}
