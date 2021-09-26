package stickman.model;

public abstract class EntityCollisionStrategy {

    protected Level level;

    public EntityCollisionStrategy(Level level) {
        this.level = level;
    }

    public abstract void collisionReact (Entity entity);
}
