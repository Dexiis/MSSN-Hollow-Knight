package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.core.SubPlot;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class TheKnight extends Entity implements IVisualizable {
    private boolean isGrounded = false;
    private static final float JUMP_STRENGTH = 500f;
    private static final float SPEED = 200f;
    public static final float ATTACK_DURANTION = 100f;

    public TheKnight(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 30, 80);
        this.mass = 1f;
        this.health = 10;
    }

    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
        isGrounded = false;
    }

    public void moveRight() {
        setVelocity(new PVector(SPEED, getVelocity().y));
    }

    public void moveLeft() {
        setVelocity(new PVector(-SPEED, getVelocity().y));
    }

    public void stopMovement() {
        setVelocity(new PVector(0, getVelocity().y));
    }

    public HurtBox attack(Direction direction) {
        HurtBox.Builder builder = new HurtBox.Builder();

        switch (direction) {
            case UP:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, 100).addPoint(25, 150).addPoint(-25, 150).addPoint(-55, 100);
                break;
            case DOWN:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, -100).addPoint(25, -150).addPoint(-25, -150).addPoint(-55, -100);
                break;
            case LEFT:
                builder.addPoint(0, -65).addPoint(0, 65).addPoint(-100, 55).addPoint(-150, 25).addPoint(-150, -25).addPoint(-100, -55);
                break;
            case RIGHT:
                builder.addPoint(0, 65).addPoint(0, -65).addPoint(100, -55).addPoint(150, -25).addPoint(150, 25).addPoint(100, 55);
                break;
            default:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, 100).addPoint(25, 150).addPoint(-25, 150).addPoint(-55, 100);
                break;
        }

        return builder.build();
    }

    public void setIsGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public boolean getIsGrounded() {
        return isGrounded;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}