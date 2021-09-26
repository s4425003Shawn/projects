package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StickManFactory extends EntityFactory {
    private final JSONArray jsonArray;
    public StickManFactory(Level level, JSONArray jsonArray) {
        super(level);
        this.jsonArray = jsonArray;
    }

    /**
     * create stick man
     * @return return list of stick man
     */
    @Override
    public List<Entity> createEntity() {
            List<Entity> stickManArrayList = new ArrayList<>();
            for (Object o : this.jsonArray) {
            JSONObject obj = (JSONObject) o;
                stickManArrayList.add(new StickMan((Double)obj.get("x"), level.getFloorHeight(), Entity.Layer.FOREGROUND, (String) obj.get("size")));
            }
            return stickManArrayList;
    }


}
