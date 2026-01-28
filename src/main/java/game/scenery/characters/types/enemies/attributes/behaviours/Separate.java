package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.Enemy;
import processing.core.PVector;

public class Separate extends Behaviour {

    public Separate(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Enemy me) {
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