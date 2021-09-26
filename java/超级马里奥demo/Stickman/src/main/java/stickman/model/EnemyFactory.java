package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyFactory extends EntityFactory{
    private final JSONArray jsonArray;
    private static final double WIDTH = 30;
    private static final double HEIGHT = 20;

    public EnemyFactory(Level level, JSONArray jsonArray) {
        super(level);
        this.jsonArray = jsonArray;
    }

    /**
     * Create enemy
     * @return return list of created entities
     */
    @Override
    public List<Entity> createEntity() {
        List<Entity> arr = new ArrayList<>();
        for (Object o : this.jsonArray) {
            JSONObject obj = (JSONObject) o;
            Enemy enemy = new Enemy((Double)obj.get("x"), (Double)obj.get("y"),HEIGHT, WIDTH, Entity.Layer.FOREGROUND, new EnemyCollisionStrategy(level));
            Random r = new Random();

            enemy.setXVel(0.1 * (r.nextBoolean()?1:-1));
            arr.add(enemy);
        }
        return arr;
    }
}
