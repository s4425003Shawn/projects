package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LevelOne implements Level{
    private static final double FLOOR_HEIGHT = 300;
    private final List<Entity> entityList = new ArrayList<>();
    private final List<Entity> removeList = new ArrayList<>();
    private final String url;

    public LevelOne(String url){
        this.url = url;
        entitySetUp();
    }

    /**
     * Traverse json file and automatically create entities
     */
    public void entitySetUp(){
        JSONObject jsonObject = null;
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(this.url));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        assert jsonObject != null;
        for(Object s: jsonObject.keySet()){
            switch(s.toString()){
                case "stickman":
                    entityList.addAll(new StickManFactory(this, (JSONArray) jsonObject.get("stickman")).createEntity());
                    break;
                case "mushroom":
                    entityList.addAll(new MushroomFactory(this,(JSONArray) jsonObject.get("mushroom")).createEntity());
                    break;
                case "platform":
                    entityList.addAll(new PlatformFactory(this,(JSONArray) jsonObject.get("platform")).createEntity());
                    break;
                case "flag":
                    entityList.addAll(new FlagFactory(this,(JSONArray) jsonObject.get("flag")).createEntity());
                    break;
                    case "enemy":
                    entityList.addAll(new EnemyFactory(this,(JSONArray) jsonObject.get("enemy")).createEntity());
                    break;
                default:
                    System.out.println("Should add new entity");
            }

        }
    }


    public List<Entity> getRemoveList() {
        return removeList;
    }

    @Override
    public List<Entity> getEntities() {
        return entityList;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public void tick() {
        AgentEntityMovementUpdate();
        floorBoundary();
        handleCollision();
        entityList.removeAll(removeList);
    }

    /**
     * make stick man stand on floor
     */
    public void floorBoundary(){

            for(AgentEntity entity: getAgentEntities()){
                if(entity.getYPos() + entity.getHeight() >= getFloorHeight()){
                entity.setYVel(0);
                entity.setYPos(getFloorHeight() - entity.getHeight());
            }

        }
    }
    @Override
    public double getFloorHeight() {
        return FLOOR_HEIGHT;
    }

    @Override
    public double getHeroX() {
        return getHero().getXPos();
    }

    @Override
    public double getHeroY() {
        return getHero().getYPos();
    }

    @Override
    public boolean jump() {
        if(getHero().getYVel() == 0){
            getHero().accelerate(0, -3);
        }
        return true;
    }

    @Override
    public boolean moveLeft() {
            getHero().setEnemyCollideFactor(1);
            getHero().accelerate(-1, 0);

        return true;
    }

    public List<AgentEntity> getAgentEntities(){
        List<AgentEntity> arr = new ArrayList<>();
        for(Entity entity: getEntities()){
            if(entity instanceof AgentEntity){
                arr.add((AgentEntity) entity);
            }
        }
        return arr;
    }

    /**
     * Update all agent entity movement
     */
    public void AgentEntityMovementUpdate(){
        for(AgentEntity entity: getAgentEntities()){
            entity.update();
        }
    }

    /**
     * handle collision for all entities
     */
    public void handleCollision(){
        for(Entity entity: getEntities()){
            entity.collideThink();
        }
    }

    /**
     * check if two entities collide
     * @param entityA entity A
     * @param entityB entity B
     * @return return true if entities collide
     */
    public boolean aabbintersect(Entity entityA, Entity entityB) {
        if (entityA.equals(entityB)) {
            return false;
        }

        return (entityA.getXPos() + entityA.getWidth() > entityB.getXPos())&&
                (entityA.getYPos() < entityB.getYPos() + entityB.getHeight())&&
                (entityA.getXPos() < entityB.getXPos() + entityB.getWidth())&&
                (entityA.getYPos() + entityA.getHeight() > entityB.getYPos());
    }


    public StickMan getHero(){
        for(Entity entity: entityList){
            if (entity instanceof StickMan){
               return (StickMan) entity;
            }
        }
        return null;
    }

    @Override
    public boolean moveRight() {
        getHero().setEnemyCollideFactor(1);
        getHero().accelerate(1, 0);

        return true;
    }

    @Override
    public boolean stopMoving() {
        getHero().setXVel(0);
        return true;
    }


    @Override
    public boolean shoot() {
        if(getHero().isAbilityActivated()){
            entityList.addAll(new BulletFactory(this).createEntity());
        }
        return true;
    }
}
