package stickman.model;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
    double getHeight();
    double getWidth();
    void tick();
    boolean aabbintersect(Entity entityA, Entity entityB);
    List<AgentEntity> getAgentEntities();
    double getFloorHeight();
    double getHeroX();
    double getHeroY();
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
    boolean shoot();
    StickMan getHero();
    List<Entity> getRemoveList();
}
