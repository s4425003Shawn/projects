package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FlagFactory extends EntityFactory{
    private final JSONArray jsonArray;
    private static final double WIDTH = 25;
    private static final double HEIGHT = 25;
    public FlagFactory(Level level, JSONArray jsonArray) {
        super(level);
        this.jsonArray = jsonArray;
    }

    /**
     * create flag
     * @param level access level to get environment information
     * @return return list of created flag
     */
    @Override
    public List<Entity> createEntity() {
        List<Entity> arr = new ArrayList<>();
        for (Object o : this.jsonArray) {
            JSONObject obj = (JSONObject) o;
            arr.add(new Flag((Double)obj.get("x"), (Double)obj.get("y"),HEIGHT, WIDTH, Entity.Layer.FOREGROUND, new FlagCollisionStrategy(level)));
        }
        return arr;
    }
}
