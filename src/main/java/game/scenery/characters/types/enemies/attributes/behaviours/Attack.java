package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Representa o comportamento de ataque de um inimigo.
 * <p>
 * Este comportamento é ativado quando o alvo entra na zona de visão próxima (NearSight).
 * O inimigo move-se diretamente para a última posição conhecida do alvo com uma velocidade de ataque específica.
 */
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

    /**
     * Calcula a velocidade desejada para executar o ataque.
     * <p>
     * Se o inimigo estiver num estado de ataque (flag {@code attacking}), retorna um vetor
     * em direção à posição alvo com a velocidade de ataque definida. Caso contrário, apenas
     * atualiza a posição do alvo.
     *
     * @param me O inimigo que está a executar o comportamento.
     * @return O vetor de velocidade de ataque ou (0,0) se o ataque terminar.
     */
    @Override
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            if (me.isAttacking()) return PVector.mult(PVector.sub(targetPosition, me.getPosition()), me.ATTACK_SPEED);
            else targetPosition = me.getEye().getTarget().getPosition();
        }

        me.setAttacking(false);
        return new PVector(0, 0);
    }

    public PVector getDesiredVelocity(Flock me) {
        return new PVector(0, 0);
    }

    /**
     * Verifica se as condições para atacar estão reunidas.
     * <p>
     * O ataque é considerado possível se o alvo estiver contido na lista de entidades
     * da visão próxima (NearSight).
     *
     * @param me O inimigo atual.
     * @return {@code true} se o alvo estiver ao alcance, {@code false} caso contrário.
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }
}