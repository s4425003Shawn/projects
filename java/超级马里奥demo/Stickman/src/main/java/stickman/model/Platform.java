package stickman.model;

public class Platform extends StaticEntity{
    private final String imagePath;
    private final double firstTileX;
    private final double lastTileX;

    public Platform(double xPos, double yPos, double height, double width, Layer layer, double firstTileX, double lastTileX, EntityCollisionStrategy strategy) {
        super(xPos, yPos, height, width, layer, strategy);
        this.imagePath = "/foot_tile.png";
        this.firstTileX = firstTileX;
        this.lastTileX = lastTileX;

    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * loop collision strategy if platform collide an enemy
     */
    @Override
    public void collideThink() {
        getStrategy().collisionReact(this);
    }

    /**
     * x position of first tile of platform
     * @return x position
     */
    public double getFirstTileX() {
        return firstTileX;
    }

    /**
     * y position of first tile of platform
     * @return y position
     */
    public double getLastTileX(){
        return lastTileX;
    }


}
