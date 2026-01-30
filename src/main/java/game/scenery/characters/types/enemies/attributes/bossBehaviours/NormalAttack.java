package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;

/**
 * Comportamento de ataque do chefe.
 */
public class NormalAttack extends Behaviour {

    /**
     * Constrói um comportamento de ataque.
     *
     * @param weight o peso
     */
    public NormalAttack(float weight) {
        super(weight);
    }

    public boolean checkBehaviour(Enemy me) {
        //System.out.println(me.getEye().getNearSight());
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }

}
