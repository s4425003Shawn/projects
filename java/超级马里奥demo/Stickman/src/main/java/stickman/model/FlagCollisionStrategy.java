package stickman.model;

public class FlagCollisionStrategy extends EntityCollisionStrategy {

    public FlagCollisionStrategy(Level level) {
        super(level);
    }

    /**
     * Flag collide with stick man make game to end
     * @param entity this flag
     */
    @Override
    public void collisionReact(Entity entity) {
        for (AgentEntity entityB: level.getAgentEntities()){
            if (level.aabbintersect(entity, entityB)){
               if (entityB instanceof StickMan){
                   System.exit(0);
               }
        }
    }
    }
}
