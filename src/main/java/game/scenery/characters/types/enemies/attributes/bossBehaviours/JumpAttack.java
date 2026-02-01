package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Comportamento de ataque com salto do chefe.
 */
public class JumpAttack extends Behaviour {

    /**
     * Constrói um comportamento de ataque com salto.
     *
     * @param weight o peso
     */
    public JumpAttack(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            float xDistance = me.getEye().getTarget().getPosition().x - me.getPosition().x;
            me.setVelocity(new PVector(xDistance / 2, xDistance));
        }
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }

}
