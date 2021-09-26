package stickman.model;

import java.util.List;

public abstract class  EntityFactory {

     protected final Level level;

     public EntityFactory(Level level) {
          this.level = level;

     }

    public abstract List<Entity> createEntity();



}
