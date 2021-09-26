package stickman.model;

public class BulletCollisionStrategy extends EntityCollisionStrategy {
    public BulletCollisionStrategy(Level level) {
        super(level);
    }

    /**
     * If bullet collide with enemy, enemy disappear and bullet disappear too
     * @param entity this bullet
     */
    @Override
    public void collisionReact(Entity entity) {
        for (Entity entityB : level.getEntities()) {
            if (level.aabbintersect(entity, entityB)) {
                if (entityB instanceof Enemy) {
                    level.getRemoveList().add(entityB);
                    level.getRemoveList().add(entity);
                }
            }
        }
    }
}
