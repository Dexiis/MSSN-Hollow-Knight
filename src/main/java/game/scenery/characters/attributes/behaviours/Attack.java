package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

public class Attack extends Behaviour {

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
        //TODO
        return new PVector(0, 0);
    }

    private boolean checkBehaviour(Entity me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }
}