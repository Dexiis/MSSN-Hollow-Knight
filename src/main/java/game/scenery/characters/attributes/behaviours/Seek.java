package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa o comportamento de procura (Seek), onde a entidade se move diretamente em direção a um alvo.
 * <p>
 * Este comportamento calcula um vetor de velocidade que aponta da posição atual da entidade
 * para a posição do alvo detetado pelo seu sensor visual (Eye).
 */
public class Seek extends Behaviour {
    private final boolean grounded;

    /**
     * Construtor do comportamento Seek.
     *
     * @param weight   O peso ou prioridade deste comportamento no sistema de decisão de movimento.
     * @param grounded Define se a entidade está presa ao chão ({@code true}), ignorando movimentos verticais, ou se pode voar ({@code false}).
     */
    public Seek(float weight, boolean grounded) {
        super(weight);
        this.grounded = grounded;
    }

    /**
     * Calcula a velocidade desejada para alcançar o alvo atual.
     * <p>
     * Obtém o alvo através do sensor da entidade. Se a flag {@code grounded} estiver ativa,
     * o vetor resultante terá a componente Y a zero, forçando o movimento apenas no eixo horizontal.
     *
     * @param me A entidade que está a executar o comportamento.
     * @return O vetor {@link PVector} que aponta na direção do alvo.
     */
    @Override
    public PVector getDesiredVelocity(Entity me) {
        Entity characterTarget = me.getEye().getTarget();
        PVector vetor = PVector.sub(characterTarget.getPosition(), me.getPosition());
        if (grounded) return new PVector(vetor.x, 0);
        return vetor;
    }
}