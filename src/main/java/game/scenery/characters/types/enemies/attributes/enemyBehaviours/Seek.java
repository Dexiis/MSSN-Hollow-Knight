package game.scenery.characters.types.enemies.attributes.enemyBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa um comportamento de procura segura (Safe Seek).
 * <p>
 * A entidade persegue o alvo apenas se este estiver visível ao longe (FarSight)
 * mas não estiver demasiado perto (NearSight vazio), mantendo assim uma distância
 * de segurança.
 * </p>
 */
public class Seek extends Behaviour {
    /**
     * Constrói um novo comportamento de procura segura com o peso especificado.
     *
     * @param weight o peso ou prioridade deste comportamento
     */
    public Seek(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para alcançar o alvo.
     * <p>
     * Se as condições de segurança forem cumpridas (alvo longe e não perto), retorna
     * o vetor direto para o alvo. Caso contrário, a entidade para (vetor nulo).
     * </p>
     *
     * @param me a entidade que está a executar o comportamento
     * @return o vetor de direção para o alvo ou (0,0) se não for seguro avançar
     */
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            Entity characterTarget = me.getEye().getTarget();

            return PVector.sub(characterTarget.getPosition(), me.getPosition());
        }
        return new PVector(0, 0);
    }

    /**
     * Verifica as condições do comportamento de procura segura.
     * <p>
     * O comportamento só é ativado se o alvo estiver no campo de visão distante (FarSight)
     * e a lista de entidades no campo de visão próximo (NearSight) estiver vazia.
     * </p>
     *
     * @param me a entidade atual
     * @return {@code true} se for seguro perseguir, {@code false} caso contrário
     */
    private boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }

}