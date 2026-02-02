package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;

/**
 * Representa o comportamento de ataque do chefe.
 * <p>
 * Esta classe define o comportamento padrão de ataque do chefe, onde o inimigo ataca quando o alvo está próximo.
 * </p>
 */
public class NormalAttack extends Behaviour {

    /**
     * Inicializa o comportamento de ataque.
     * <p>
     * Cria uma instância do comportamento NormalAttack com o peso especificado, herdando do comportamento base.
     * </p>
     *
     * @param weight o peso do comportamento
     */
    public NormalAttack(float weight) {
        super(weight);
    }

    /**
     * Verifica se o comportamento deve ser ativado.
     * <p>
     * Avalia se o alvo está dentro do campo de visão próxima do inimigo, determinando se o comportamento de ataque deve ser executado.
     * </p>
     *
     * @param me o inimigo que verifica o comportamento
     * @return verdadeiro se o comportamento deve ser ativado, falso caso contrário
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }

}
