package game.scenery.characters.types.enemies.attributes.enemyBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa o comportamento de ataque de um inimigo.
 * <p>
 * Este comportamento é ativado quando o alvo entra na zona de visão próxima (NearSight).
 * O inimigo move-se diretamente para a última posição conhecida do alvo com uma
 * velocidade de ataque específica.
 * </p>
 */
public class Attack extends Behaviour {
    private PVector targetPosition;
    private PVector chargeDirection;

    /**
     * Constrói um novo comportamento de ataque com o peso especificado.
     *
     * @param weight o peso ou prioridade deste comportamento no sistema de decisão de movimento
     */
    public Attack(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para executar o ataque.
     * <p>
     * Se o inimigo estiver num estado de ataque (flag {@code attacking}), retorna um vetor
     * em direção à posição alvo com a velocidade de ataque definida. Caso contrário, apenas
     * atualiza a posição do alvo.
     * </p>
     *
     * @param me o inimigo que está a executar o comportamento
     * @return o vetor de velocidade de ataque ou (0,0) se o ataque terminar
     */
    @Override
    public PVector getDesiredVelocity(Enemy me) {
        if (chargeDirection != null) return chargeDirection;
        return new PVector();
    }

    /**
     * Verifica se as condições para atacar estão reunidas.
     * <p>
     * O ataque é considerado possível se o alvo estiver contido na lista de entidades
     * da visão próxima (NearSight).
     * </p>
     *
     * @param me o inimigo atual
     * @return {@code true} se o alvo estiver ao alcance, {@code false} caso contrário
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }

    public void saveTargetPosition(Enemy me) {
        this.targetPosition = me.getEye().getTarget().getPosition();
        saveVelocity(me);
    }

    private void saveVelocity(Enemy me) {
        this.chargeDirection = PVector.sub(targetPosition, me.getPosition()).normalize().mult(me.getDna().getMaxSpeed());
    }

}