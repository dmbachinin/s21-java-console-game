package edu.school21.game.entities;

import edu.school21.chase.ChaseType;
import edu.school21.chase.MovementDirection;
import edu.school21.chase.heplers.Pair;

import java.util.Random;

public class Enemy {
    private Pair<Integer, Integer> position;
    private ChaseType type;
    private MovementDirection lastMove;

    public Enemy(Pair<Integer, Integer> startPos) {
        Random rnd = new Random();
        int randInt = rnd.nextInt(1, 12);
        if (randInt <= 3) {
            type = ChaseType.STALKER;
        } else if (randInt <= 6) {
            type = ChaseType.SCOUT;
        } else if (randInt <= 8) {
            type = ChaseType.HUNTER;
        } else if (randInt <= 10) {
            type = ChaseType.SECURITY;
        } else {
            type = ChaseType.HYBRID;
        }
        position = startPos;
    }

    public Enemy(Pair<Integer, Integer> startPos, ChaseType type) {
        this.type = type;
        position = startPos;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public MovementDirection getLastMove() {
        return lastMove;
    }

    public void setLastMove(MovementDirection lastMove) {
        this.lastMove = lastMove;
    }

    public ChaseType getChaseType() {
        return type;
    }
}
