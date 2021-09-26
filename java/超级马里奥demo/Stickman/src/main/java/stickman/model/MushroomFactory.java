package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MushroomFactory extends EntityFactory{
    private final JSONArray jsonArray;
    private static final double WIDTH = 25;
    private static final double HEIGHT = 25;
    public MushroomFactory(Level level, JSONArray jsonArray) {
        super(level);
        this.jsonArray = jsonArray;
    }

    /**
     * create mushrooms
     * @return return list of mushrooms
     */
    @Override
    public List<Entity> createEntity() {
        List<Entity> arr = new ArrayList<>();
        for (Object o : this.jsonArray) {
            JSONObject obj = (JSONObject) o;
            arr.add(new Mushroom((Double) obj.get("x"), (Double) obj.get("y"), HEIGHT, WIDTH, Entity.Layer.FOREGROUND, new MushroomCollisionStrategy(level)));

        }
        return arr;
    }

}
