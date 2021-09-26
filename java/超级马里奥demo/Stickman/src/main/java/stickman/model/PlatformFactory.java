package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlatformFactory extends EntityFactory {
    private final JSONArray jsonArray;
    private static final double WIDTH = 15;
    private static final double HEIGHT = 15;
    public PlatformFactory(Level level, JSONArray jsonArray) {
        super(level);
        this.jsonArray = jsonArray;
    }

    /**
     * create platform
     * @return list of platforms
     */
    @Override
    public List<Entity> createEntity() {
        List<Entity> arr = new ArrayList<>();
        for (Object o : this.jsonArray) {
            JSONObject obj = (JSONObject) o;
            double platformLength = (Double) obj.get("length");
            for(int i = 0; i < platformLength; i++){
                double widthAccumulate = WIDTH;
                widthAccumulate *= i;
                arr.add(new Platform((Double) obj.get("x") + widthAccumulate, (Double) obj.get("y"),HEIGHT, WIDTH, Entity.Layer.FOREGROUND , (Double) obj.get("x"), (Double) obj.get("x") + (platformLength) * WIDTH, new PlatformCollisionStrategy(level)));
            }
        }
        return arr;
    }
}
