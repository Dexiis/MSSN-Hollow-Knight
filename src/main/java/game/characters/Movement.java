package game.characters;

import processing.core.PVector;

public abstract class Movement {

    protected PVector position;
    protected PVector velocity = new PVector(0, 0);
    protected PVector acceleration = new PVector();
    protected float mass;

    protected Movement(PVector position) {
        this.position = position.copy();
    }

    public void applyForce(PVector force) {
        acceleration.add(PVector.div(force, mass));
    }

    public void move(float dt) {
        velocity.add(acceleration.mult(dt));
        position.add(PVector.mult(velocity, dt));
        acceleration.mult(0);
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getPosition() {
        return position;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public float getMass() {
        return mass;
    }

}