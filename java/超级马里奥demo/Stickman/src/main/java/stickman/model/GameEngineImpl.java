package stickman.model;

public class GameEngineImpl implements GameEngine{
    private Level level;
    private final String url;
    public GameEngineImpl(String url) {
        this.url = url;
    }

    @Override
    public Level getCurrentLevel() {
        return this.level;
    }

    @Override
    public void startLevel() {
        this.level = new LevelOne(this.url);
    }

    @Override
    public boolean jump() {
        return getCurrentLevel().jump();
    }

    @Override
    public boolean moveLeft() {
        return getCurrentLevel().moveLeft();
    }

    @Override
    public boolean moveRight() {
        return getCurrentLevel().moveRight();
    }

    @Override
    public boolean stopMoving() {
        return getCurrentLevel().stopMoving();
    }

    @Override
    public boolean shoot() {
        return getCurrentLevel().shoot();
    }

    @Override
    public void tick() {
        level.tick();

    }
}
