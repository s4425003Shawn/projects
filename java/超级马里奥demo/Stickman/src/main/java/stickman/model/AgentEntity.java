package stickman.model;

public abstract class AgentEntity implements Entity{
    private static final double GRAVITY = 0.05;
    private double xPos;
    private double yPos;
    private double height;
    private double width;
    private final Layer layer;
    private double xVel = 0;
    private double yVel = 0;
    private final EntityCollisionStrategy strategy;



    /**
     * The entity able to move in game
     * @param xPos x position
     * @param yPos y position
     * @param height height of entity
     * @param width width of entity
     * @param layer view order
     */
    public AgentEntity(double xPos, double yPos, double height, double width, Layer layer, EntityCollisionStrategy strategy) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        this.layer = layer;
        this.strategy = strategy;
    }

    public AgentEntity(double xPos, double yPos, Layer layer) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.layer = layer;
        this.strategy = null;
    }

    /**
     * assign acceleration entity to change velocity
     * @param accelerationX acceleration to change x velocity
     * @param accelerationY acceleration to change y velocity
     */
    public void accelerate(double accelerationX, double accelerationY) {
        xVel += accelerationX;
        yVel += accelerationY;

    }

    /**
     * Move entity based in pixel
     * @param xDelta move x position
     * @param yDelta move y position
     */
    public void move(double xDelta, double yDelta) {
        xPos += xDelta;
        yPos += yDelta;
    }

    public EntityCollisionStrategy getStrategy() {
        return strategy;
    }
    /**
     * update location based on x and y velocity also assign downward gravity
     */
    public void update() {
        move(xVel, yVel);
        accelerate(0, GRAVITY);


    }

    public double getXVel() {
        return xVel;
    }

    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public void setYVel(double yVel) {
        this.yVel = yVel;
    }



    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    public void setYPos(double yPos) {
        this.yPos = yPos;
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

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
