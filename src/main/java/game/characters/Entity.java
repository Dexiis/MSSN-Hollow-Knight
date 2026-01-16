package game.characters;

import game.characters.attributes.Behaviour;
import game.characters.attributes.DNA;
import game.characters.attributes.Eye;
import game.hitbox.Hitbox;
import processing.core.PVector;

import java.util.List;

public class Entity extends Movement {

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

    //TODO isDead()

    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) {
            hitbox.setPosition(position);
        }
    }

    @Override
    public void setPosition(PVector position) {
        this.position = position;
        this.hitbox.setPosition(position);
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
        if (eye != null) eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    public void applyBehaviours(List<Behaviour> behaviours, float dt) {
        if (eye != null) eye.look();
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

}