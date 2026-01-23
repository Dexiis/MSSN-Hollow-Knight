package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

public class Attack extends Behaviour {

    private PVector targetPosition;

    /**
     * Construtor do comportamento Attack.
     *
     * @param weight O peso ou prioridade deste comportamento no sistema de decisão de movimento.
     */
    public Attack(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Entity me) {
        if(checkBehaviour(me)) {
            if(me.isAttacking()) {
                return PVector.mult(PVector.sub(targetPosition, me.getPosition()), 10);
            } else {
                targetPosition = me.getEye().getTarget().getPosition();
            }
        }

        me.setAttacking(false);
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Entity me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }
}