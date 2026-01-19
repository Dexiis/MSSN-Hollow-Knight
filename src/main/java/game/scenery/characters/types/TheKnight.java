package game.scenery.characters.types;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class TheKnight extends Entity implements IVisualizable {
    private static final float JUMP_STRENGTH = 550f;
    private static final float SPEED = 275f;
    public static final float ATTACK_DURANTION = 100f;

    private boolean isGrounded = false;
    private Direction direction;

    public static final int SPRITE_SIZE = 80;
    private PImage sprite;

    public TheKnight(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 30, 80);
        this.mass = 1f;
        this.health = 10;
    }

    //TODO RETORNAR PVECTOR
    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
    }

    //TODO RETORNAR PVECTOR
    public void moveRight() {
        setVelocity(new PVector(Math.min(SPEED, getVelocity().x + SPEED), getVelocity().y));
    }

    //TODO RETORNAR PVECTOR
    public void moveLeft() {
        setVelocity(new PVector(Math.max(-SPEED, getVelocity().x - SPEED), getVelocity().y));
    }

    //TODO RETORNAR PVECTOR
    public void stopMovement() {
        setVelocity(new PVector(0.5f * getVelocity().x, getVelocity().y));
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

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        //this.hitbox.draw(painter, plt);

        int multValue = 1;
        if(direction == Direction.LEFT) multValue = -1;

        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        p.pushMatrix();

        // O (+5) é apenas uma correção para colocar a sprite encostada ao chão
        p.translate(pp[0] - multValue * SPRITE_SIZE / 2f, pp[1] - SPRITE_SIZE / 2f + 5);
        p.scale(multValue, 1);
        p.image(this.sprite, 0, 0);

        p.popMatrix();


    }
}