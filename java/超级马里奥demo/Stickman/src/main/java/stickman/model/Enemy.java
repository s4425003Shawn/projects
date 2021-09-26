package stickman.model;

public class Enemy extends AgentEntity{
    private final String imagePath;


    public Enemy(double xPos, double yPos, double height, double width, Layer layer, EntityCollisionStrategy strategy) {
        super(xPos, yPos, height, width, layer, strategy);
        this.imagePath = "/slimeBa.png";
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * loop collision strategy if enemy collide an enemy
     */
    @Override
    public void collideThink() {
        getStrategy().collisionReact(this);
    }


}
