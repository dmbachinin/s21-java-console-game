package edu.school21.game.entities;

import edu.school21.chase.ChaseType;
import edu.school21.chase.MovementDirection;
import edu.school21.game.heplers.Pair;

public class Hero {
    private int xPos;
    private int yPos;

    public Hero(Pair<Integer, Integer> pos) {
        xPos = pos.getFirst();
        yPos = pos.getSecond();
    }

    public void moveTo(MovementDirection direction) {
        switch (direction) {
            case UP:
                yPos--;
                break;
            case DOWN:
                yPos++;
                break;
            case LEFT:
                xPos--;
                break;
            case RIGHT:
                xPos++;
                break;
            default:
                break;
        }
    }

    public Pair<Integer, Integer> getPos() {
        return new Pair<>(xPos, yPos);
    }
}
