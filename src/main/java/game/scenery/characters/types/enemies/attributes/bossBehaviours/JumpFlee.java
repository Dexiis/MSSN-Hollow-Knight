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
                PVector toCenter = PVector.sub(new PVector(6000, 0), me.getPosition()).normalize().mult(1000);
                me.setVelocity(new PVector(toCenter.x, 650));
            } else {
                float xDistance = me.getPosition().x - me.getEye().getTarget().getPosition().x;
                me.setVelocity(new PVector((300 / xDistance) * 500, 400));
            }
        }
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }

}
