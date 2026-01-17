package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

public class Seek extends Behaviour {

    public Seek(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Entity me) {
        Entity characterTarget = me.getEye().getTarget();

        return PVector.sub(characterTarget.getPosition(), me.getPosition());
    }

}