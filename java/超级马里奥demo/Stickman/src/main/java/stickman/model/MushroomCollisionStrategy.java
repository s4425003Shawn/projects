package stickman.model;

public class MushroomCollisionStrategy extends EntityCollisionStrategy{

    public MushroomCollisionStrategy(Level level) {
        super(level);
    }

    /**
     * Mushroom collides with Stick man enhance stick man's ability so stickman
     * can shoot bullets.
     * @param entity this mushroom
     */
    @Override
    public void collisionReact(Entity entity) {
        for (AgentEntity entityB: level.getAgentEntities()) {
            if (level.aabbintersect(entity, entityB)) {
                if(entityB instanceof StickMan){
                    ((StickMan) entityB).setAbilityActivated(true);
                    level.getRemoveList().add(entity);
                }
            }
        }
    }


}
