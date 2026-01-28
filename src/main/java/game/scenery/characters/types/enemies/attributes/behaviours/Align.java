package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.Enemy;
import processing.core.PVector;

public class Align extends Behaviour {

    public Align(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Enemy me) {
        PVector desiredVelocity = me.getVelocity().copy();
        for (Entity character : me.getEye().getFarSight())
            desiredVelocity.add(character.getVelocity());

        return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
    }
}
