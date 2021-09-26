package stickman.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface GameEngine {
    Level getCurrentLevel();

    void startLevel();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
    boolean shoot();

    void tick();
}
