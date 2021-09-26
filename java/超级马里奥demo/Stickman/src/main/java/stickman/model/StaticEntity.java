package stickman.model;

public abstract class StaticEntity implements Entity{
    private final double xPos;
    private final double yPos;
    private final double height;
    private final double width;
    private final Layer layer;
    private final EntityCollisionStrategy strategy;
    public StaticEntity(double xPos, double yPos, double height, double width, Layer layer, EntityCollisionStrategy strategy) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        this.layer = layer;
        this.strategy = strategy;
    }

    public EntityCollisionStrategy getStrategy() {
        return strategy;
    }
    @Override
    public abstract String getImagePath();

    @Override
    public double getXPos() {
        return this.xPos;
    }

    @Override
    public double getYPos() {
        return this.yPos;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }
}
