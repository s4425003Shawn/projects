package stickman.model;

public class Flag extends StaticEntity {
    private final String imagePath;
    public Flag(double xPos, double yPos, double height, double width, Layer layer, EntityCollisionStrategy strategy) {
        super(xPos, yPos, height, width, layer, strategy);
        this.imagePath = "/flag.png";

    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * loop collision strategy if flag collide an stick man
     */
    @Override
    public void collideThink() {
        getStrategy().collisionReact(this);

    }
}
