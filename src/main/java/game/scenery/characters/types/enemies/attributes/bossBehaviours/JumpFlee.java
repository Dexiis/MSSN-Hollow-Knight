package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Comportamento de fuga com salto do chefe.
 */
public class JumpFlee extends Behaviour {

    /**
     * Constrói um comportamento de fuga com salto.
     *
     * @param weight o peso
     */
    public JumpFlee(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Enemy me) {
        float g = 1280f;
        float theta = (float) Math.toRadians(50);
        float xDistance;
        float direction;
        if (checkBehaviour(me)) {
            if (((FalseKnight) me).isWalled()) {
                PVector center = new PVector(6000, me.getPosition().y);

                xDistance = center.x - me.getPosition().x;
                direction = xDistance / Math.abs(xDistance);
                xDistance = Math.abs(xDistance) - me.getHitbox().getWidth() / 2;
            } else {
                xDistance = me.getEye().getTarget().getPosition().x - me.getPosition().x;
                direction = -(xDistance / Math.abs(xDistance));
            }

            super.calculateTrajectory(me, g, theta, xDistance, direction);
        }
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }

}
