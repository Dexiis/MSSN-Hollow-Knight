package game.characters;

import game.characters.attributes.*;
import game.core.*;
import game.characters.hitbox.Hitbox;
import game.characters.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

public abstract class Entity extends Movement implements IVisualizable, Runnable {

    protected float[] positions;
    protected Eye eye;
    protected DNA dna;
    protected float phiWander;
    private double[] window;

    protected int health;
    protected Hitbox hitbox;

    protected Entity(PVector position) {
        super(position);
    }

    //TODO isDead(), hurt()
    
    @Override
    public void run() {

    }

    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) {
            hitbox.setPosition(new Point(position.x, position.y));
        }
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public Eye getEye() {
        return this.eye;
    }

    public void setEye(Eye eye) {
        this.eye = eye;
    }

    public void applyBehaviour(Behaviour behaviour, float dt) {
        if (eye != null)
            eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    public void applyBehaviours(List<Behaviour> behaviours, float dt) {
        if (eye != null)
            eye.look();
        PVector vd = new PVector();
        float sumWeights = 0;
        for (Behaviour behaviour : behaviours)
            sumWeights += behaviour.getWeight();

        for (Behaviour behaviour : behaviours) {
            PVector vdd = behaviour.getDesiredVelocity(this);
            vdd.mult(behaviour.getWeight() / sumWeights);
            vd.add(vdd);
        }
        move(dt, vd);
    }

    public float getPhiWander() {
        return phiWander;
    }

    public void setPhiWander(float newPhiWander) {
        this.phiWander = newPhiWander;
    }

    public DNA getDNA() {
        return dna;
    }

    public void move(float dt, PVector vd) {
        vd.normalize().mult(dna.getMaxSpeed());
        PVector fs = PVector.sub(vd, velocity);
        applyForce(fs.limit(dna.getMaxForce()));
        move(dt);
    }

    public abstract void display(PApplet p, SubPlot plt);
}