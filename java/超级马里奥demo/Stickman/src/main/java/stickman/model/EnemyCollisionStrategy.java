package stickman.model;

public class EnemyCollisionStrategy extends EntityCollisionStrategy {
    public EnemyCollisionStrategy(Level level) {
        super(level);
    }

    /**
     * Enemy reduce stick man's health 1 everytime collide with it.
     * Also stick man will be pushing back when colliding.
     * @param entity this enemy
     */
    @Override
    public void collisionReact(Entity entity) {
        for (AgentEntity entityB: level.getAgentEntities()){
            if (level.aabbintersect(entity, entityB)){
                if (entityB instanceof StickMan){
                    ((StickMan) entityB).setHealth(((StickMan) entityB).getHealth() -1 );
                    System.out.println("Health :" + ((StickMan) entityB).getHealth());

                    if(((StickMan) entityB).getHealth() == 0){
                        System.exit(0);
                    }
                    if (entity.getXPos() + entity.getWidth()/2 < entityB.getXPos() + entityB.getWidth()/2){
                        entityB.setXPos(entity.getXPos() + entity.getWidth());

                        ((StickMan) entityB).setEnemyCollideFactor(0.99);
                        entityB.setXVel(0.5);
                        entityB.setYVel(-1);
                    }else if(entity.getXPos() + entity.getWidth()/2 > entityB.getXPos() + entityB.getWidth()/2){
                        entityB.setXPos(entity.getXPos() - entityB.getWidth());
                        ((StickMan) entityB).setEnemyCollideFactor(0.99);
                        entityB.setXVel(-0.5);
                        entityB.setYVel(-1);


                    }
                }
            }
    }
}}
