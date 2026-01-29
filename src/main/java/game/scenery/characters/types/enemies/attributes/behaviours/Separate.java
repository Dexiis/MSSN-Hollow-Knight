package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

public class Separate extends Behaviour {
    public Separate(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = new PVector();
        for (Flock f : me.getEye().getNearSight()) {
            PVector direction = me.getToroidalDistanceVector(f.getPosition()).mult(-1);
            float dir = direction.mag();
            direction.div(dir * dir);
            desiredVelocity.add(direction);
        }

        return desiredVelocity;
    }

    public PVector getDesiredVelocity(Enemy me) {
        return new PVector(0, 0);
    }
}