package game.scenery.characters.attributes.behaviours;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa o comportamento de procura agressiva (Agressive Seek).
 * <p>
 * Ao contrário do Seek padrão, este comportamento verifica se o alvo está dentro
 * de um campo de visão específico (FarSight) antes de iniciar a perseguição.
 * Além disso, o movimento resultante é restrito exclusivamente ao eixo horizontal.
 */
public class AgressiveSeek extends Behaviour {

    /**
     * Construtor do comportamento AgressiveSeek.
     *
     * @param weight O peso ou prioridade deste comportamento no sistema de decisão de movimento.
     */
    public AgressiveSeek(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para perseguir o alvo, caso este esteja visível.
     * <p>
     * Se a verificação de comportamento passar (alvo à vista), calcula um vetor em direção ao alvo,
     * anulando a componente vertical (Y) para forçar a entidade a mover-se apenas pelo chão.
     * Caso contrário, retorna um vetor nulo (sem movimento).
     *
     * @param me A entidade que está a executar o comportamento.
     * @return O vetor de velocidade horizontal em direção ao alvo ou (0,0) se o alvo não for válido.
     */
    @Override
    public PVector getDesiredVelocity(Entity me) {
        if (checkBehaviour(me)) {
            Entity characterTarget = me.getEye().getTarget();
            PVector vetor = PVector.sub(characterTarget.getPosition(), me.getPosition());

            return new PVector(vetor.x, 0);
        }
        return new PVector(0, 0);
    }

    /**
     * Verifica se as condições para a perseguição agressiva estão reunidas.
     * Valida se o alvo atual está contido na área de visão distante (FarSight) do sensor da entidade.
     *
     * @param me A entidade atual.
     * @return {@code true} se o alvo estiver no campo de visão, {@code false} caso contrário.
     */
    private boolean checkBehaviour(Entity me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }
}