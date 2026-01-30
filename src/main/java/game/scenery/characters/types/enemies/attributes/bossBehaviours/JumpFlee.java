package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
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
        if (checkBehaviour(me))
            me.setVelocity(new PVector((me.getPosition().x - me.getEye().getTarget().getPosition().x) * 2 / 3, 300));
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }

}
