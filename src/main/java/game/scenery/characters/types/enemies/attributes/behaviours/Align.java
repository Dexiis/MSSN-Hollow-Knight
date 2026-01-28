package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

public class Align extends Behaviour {
    public Align(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = me.getVelocity().copy();
        for (Flock character : me.getEye().getFarSight())
            desiredVelocity.add(character.getVelocity());

        return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
    }

    public PVector getDesiredVelocity(Enemy me) {
        return new PVector(0, 0);
    }
}
