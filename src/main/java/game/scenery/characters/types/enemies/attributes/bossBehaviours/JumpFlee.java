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
        if (checkBehaviour(me)) {
            if (((FalseKnight) me).isWalled()) {
                PVector toCenter = PVector.sub(new PVector(6000, 0), me.getPosition());
                me.setVelocity(new PVector(toCenter.x, 350));
            } else {
                float xDistance = me.getPosition().x - me.getEye().getTarget().getPosition().x;
                me.setVelocity(new PVector((500 / xDistance) * 300, 300));
            }
        }
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }

}
