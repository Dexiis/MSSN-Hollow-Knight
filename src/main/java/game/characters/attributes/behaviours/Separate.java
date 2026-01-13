package game.characters.attributes.behaviours;

import game.characters.Entity;
import game.characters.attributes.Behaviour;
import processing.core.PVector;

public class Separate extends Behaviour {

    public Separate(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Entity me) {
        PVector desiredVelocity = new PVector();
        for (Entity character : me.getEye().getNearSight()) {
            PVector direction = PVector.sub(me.getPosition(), character.getPosition());
            float dir = direction.mag();
            direction.div(dir * dir);
            desiredVelocity.add(direction);
        }

        return desiredVelocity;
    }

}