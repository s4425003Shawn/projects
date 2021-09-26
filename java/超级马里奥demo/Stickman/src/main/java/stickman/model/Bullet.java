package stickman.model;

public class Bullet extends AgentEntity{
    private final String imagePath;


    public Bullet(double xPos, double yPos, double height, double width, Layer layer, EntityCollisionStrategy strategy) {
        super(xPos, yPos, height, width, layer, strategy);
        this.imagePath = "/bullet.png";

    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * update bullet move without gravity assign
     */
    @Override
    public void update() {
        move(getXVel(), getYVel());
    }

    /**
     * loop collision strategy if bullet collide an entity
     */
    @Override
    public void collideThink() {
        getStrategy().collisionReact(this);

    }
}
