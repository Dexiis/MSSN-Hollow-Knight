package game.characters.types;

import game.characters.Entity;
import game.characters.IVisualizable;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PVector;

public class TheKnight extends Entity implements IVisualizable {
    private boolean isGrounded = false;
    private static final float JUMP_STRENGTH = -500f;
    private static final float SPEED = 200f;

    public TheKnight(PVector position) {
        super(position);
        this.hitbox = new Hitbox(position, 30, 70);
        this.mass = 1f;
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

    public void setIsGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public boolean getIsGrounded() {
        return isGrounded;
    }

    @Override
    public void display(PApplet p, SubPlot plt) {
        //TODO Sprites??

        p.pushStyle();
        p.stroke(255, 0, 0);
        p.strokeWeight(5);

        float[] pp = plt.getPixelCoord(position.x, position.y);
        p.point(pp[0], pp[1]);

        p.popStyle();

        this.hitbox.display(p, plt);
    }
}