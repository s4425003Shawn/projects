package stickman.model;

import java.util.ArrayList;
import java.util.List;

public class BulletFactory extends EntityFactory{

    private static final double WIDTH = 20;
    private static final double HEIGHT = 20;

    public BulletFactory(Level level) {
        super(level);
    }

    /**
     * Create Bullet entity
     * @return a list of bullets added
     */
    public List<Entity> createEntity() {
        List<Entity> arr = new ArrayList<>();
        Bullet bullet = new Bullet(level.getHeroX(), level.getHeroY() + level.getHero().getHeight()/2 - HEIGHT/2,
                HEIGHT, WIDTH, Entity.Layer.FOREGROUND, new BulletCollisionStrategy(level));
        bullet.setYVel(0);
        for(Entity entity: level.getEntities()){
            if (entity instanceof StickMan){
                if(((StickMan) entity).getXVel() >= 0){
                    bullet.setXVel(1.5);
                }else{
                    bullet.setXVel(-1.5);
                }
            }
        }
        arr.add(bullet);
        return arr;

    }
}
