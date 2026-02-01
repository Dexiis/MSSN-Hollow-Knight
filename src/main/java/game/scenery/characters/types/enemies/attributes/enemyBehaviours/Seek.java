package game.scenery.characters.types.enemies.attributes.enemyBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.mobs.Squit;
import processing.core.PVector;

/**
 * Representa uma conduta de perseguição cautelosa.
 * <p>
 * A entidade segue o alvo caso este se encontre no campo de visão distante,
 * mas interrompe o movimento se houver aproximação excessiva (campo de visão próximo vazio),
 * garantindo assim uma margem de segurança.
 * </p>
 */
public class Seek extends Behaviour {
    /**
     * Inicializa uma nova instância deste comportamento com a prioridade indicada.
     * <p>
     * Atribui o peso fornecido à superclasse para influenciar a decisão da entidade.
     * </p>
     *
     * @param weight o peso ou a prioridade desta ação.
     */
    public Seek(float weight) {
        super(weight);
    }

    /**
     * Determina a velocidade necessária para atingir o alvo.
     * <p>
     * Caso as condições de segurança sejam validadas (alvo longe e nada perto), devolve
     * o vetor direcionado ao alvo. Se não for seguro, a entidade permanece parada (vetor nulo).
     * </p>
     *
     * @param me a entidade inimiga que executa a ação.
     * @return o vetor de direção para o alvo ou (0,0) se não for seguro avançar.
     */
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            Entity characterTarget = me.getEye().getTarget();

            return PVector.sub(characterTarget.getPosition(), me.getPosition());
        }
        return new PVector(0, 0);
    }

    /**
     * Avalia se as condições para a perseguição segura estão reunidas.
     * <p>
     * Esta operação só permite o movimento se o alvo estiver identificado no campo de visão
     * distante (FarSight) e a lista de entidades no campo de visão próximo (NearSight)
     * se encontrar vazia.
     * </p>
     *
     * @param me a entidade atual sob avaliação.
     * @return {@code true} se a perseguição for segura, {@code false} caso contrário.
     */
    private boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }

}