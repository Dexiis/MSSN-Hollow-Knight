package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Comportamento de ataque do chefe.
 */
public class BossAttack extends Behaviour {

    /**
     * Constrói um comportamento de ataque.
     *
     * @param weight o peso
     */
    public BossAttack(float weight) {
        super(weight);
    }

    public PVector getDesiredVelocity(Enemy me) { //TODO Fazer lógica
        return new PVector(0, 0);
    }

    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }

}
