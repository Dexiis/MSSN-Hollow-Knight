package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

public class JumpAttack extends Behaviour {

    public JumpAttack(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            float xDistance = me.getEye().getTarget().getPosition().x - me.getPosition().x;
            float direction = xDistance / Math.abs(xDistance);
            xDistance = Math.abs(xDistance) - me.getHitbox().getWidth() / 2;
            float g = 1280f;
            float theta = (float) Math.toRadians(30);

            super.calculateTrajectory(me, g, theta, xDistance, direction);
        }
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }

}