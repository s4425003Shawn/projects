package stickman.model;

public class PlatformCollisionStrategy extends EntityCollisionStrategy{

    public PlatformCollisionStrategy(Level level) {
        super(level);
    }

    /**
     * platform collide with agent entity will block them for intersecting.
     * @param level access level to get environment information
     * @param entity this platform
     */
    @Override
    public void collisionReact(Entity entity) {
        for (AgentEntity entityB: level.getAgentEntities()){
                if (level.aabbintersect(entity, entityB)){
                    if(entityB instanceof Bullet){
                        level.getRemoveList().add(entityB);
                    }
                    if(entityB.getXPos() + entityB.getWidth() <= ((Platform) entity).getFirstTileX() + 1 ){
                        entityB.setXPos(((Platform) entity).getFirstTileX() - entityB.getWidth());

                    }else if(entityB.getXPos() >= ((Platform) entity).getLastTileX() - 1){

                        entityB.setXPos(((Platform) entity).getLastTileX());
                    }
                    else{
                        if(entityB.getYPos() + entityB.getHeight()/2 > entity.getYPos()+entity.getHeight()){
                            entityB.setYVel(0);
                            entityB.setYPos(entity.getYPos() + entity.getHeight());
                        }else if(entityB.getYPos() + entityB.getHeight()/2 < entity.getYPos()){
                            entityB.setYVel(0);

                            entityB.setYPos(entity.getYPos() - entityB.getHeight() +0.02);


                        }
                    }
                }
            }
    }
}

