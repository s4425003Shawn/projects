package stickman.model;

public class StickMan extends AgentEntity{
    private int health = 3;
    private final String imagePath;
    private boolean abilityActivated = false;
    private double enemyCollideFactor = 1;

    public StickMan(double xPos, double yPos, Layer layer, String size) {
        super(xPos, yPos, layer);
        size(size);
        this. imagePath = "/ch_stand1.png";

    }

    /**
     * Size stick man size according to json file config
     * @param size return size of hero
     */
    public void size(String size){
        if(size.equals("normal")){
            setHeight(30);
            setWidth(15);
        }else if(size.equals("large")){
            setHeight(50);
            setWidth(50);
        }
    }

    public boolean isAbilityActivated() {
        return abilityActivated;
    }

    public void setAbilityActivated(boolean abilityActivated) {
        this.abilityActivated = abilityActivated;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void update() {
        super.update();
        setXVel(getXVel() * enemyCollideFactor);

    }

    public void setEnemyCollideFactor(double enemyCollideFactor) {
        this.enemyCollideFactor = enemyCollideFactor;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void collideThink() {

    }
}
