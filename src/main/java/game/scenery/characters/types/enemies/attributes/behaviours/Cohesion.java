package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

public class Cohesion extends Behaviour {
    public Cohesion(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Flock me) {
        PVector target = me.getPosition().copy();
        for (Flock f : me.getEye().getFarSight())
            target.add(f.getPosition());
        target.div(me.getEye().getFarSight().size() + 1);

        return me.getToroidalDistanceVector(target);
    }

    public PVector getDesiredVelocity(Enemy me) {
        return new PVector(0, 0);
    }
}