package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa um comportamento de procura segura (Safe Seek).
 * <p>
 * A entidade persegue o alvo apenas se este estiver visível ao longe (FarSight)
 * mas não estiver demasiado perto (NearSight vazio), mantendo assim uma distância de segurança.
 */
public class SafeSeek extends Behaviour {

    /**
     * Construtor do comportamento SafeSeek.
     *
     * @param weight O peso ou prioridade deste comportamento.
     */
    public SafeSeek(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para alcançar o alvo.
     * <p>
     * Se as condições de segurança forem cumpridas (alvo longe e não perto), retorna
     * o vetor direto para o alvo. Caso contrário, a entidade para (vetor nulo).
     *
     * @param me A entidade que está a executar o comportamento.
     * @return O vetor de direção para o alvo ou (0,0) se não for seguro avançar.
     */
    @Override
    public PVector getDesiredVelocity(Entity me) {
        if (checkBehaviour(me)) {
            Entity characterTarget = me.getEye().getTarget();

            return PVector.sub(characterTarget.getPosition(), me.getPosition());
        }
        return new PVector(0, 0);
    }

    /**
     * Verifica as condições do comportamento.
     * <p>
     * O comportamento só é ativado se o alvo estiver no campo de visão distante (FarSight)
     * E a lista de entidades no campo de visão próximo (NearSight) estiver vazia.
     *
     * @param me A entidade atual.
     * @return {@code true} se for seguro perseguir, {@code false} caso contrário.
     */
    private boolean checkBehaviour(Entity me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }
}