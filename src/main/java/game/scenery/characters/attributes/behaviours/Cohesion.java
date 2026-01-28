package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import game.scenery.characters.types.Enemy;
import processing.core.PVector;

public class Cohesion extends Behaviour {

    public Cohesion(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Enemy me) {
        PVector target = me.getPosition().copy();
        for (Entity character : me.getEye().getFarSight())
            target.add(character.getPosition());
        target.div(me.getEye().getFarSight().size() + 1);

        return PVector.sub(target, me.getPosition());
    }

}