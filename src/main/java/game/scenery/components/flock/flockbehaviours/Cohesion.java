package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Implementa o comportamento de Coesão (Cohesion) para agentes de bando.
 * <p>
 * Este comportamento faz com que a entidade se sinta atraída para a posição média
 * (centro de massa) dos seus vizinhos locais, mantendo o grupo unido.
 * </p>
 */
public class Cohesion extends Behaviour {

    /**
     * Constrói uma nova instância do comportamento de Coesão.
     *
     * @param weight o peso de influência deste comportamento na decisão final de movimento
     */
    public Cohesion(float weight) {
        super(weight);
    }

    /**
     * Calcula a direção para o centro geométrico dos vizinhos locais.
     * <p>
     * Soma as posições de todos os vizinhos visíveis para encontrar o ponto médio
     * e devolve o vetor que aponta da posição atual da entidade para esse centro.
     * </p>
     *
     * @param me a entidade do bando que está a ser atualizada
     * @return o vetor que aponta para o centro de massa do grupo vizinho
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector target = me.getPosition().copy();
        for (Flock f : me.getEye().getFarSight())
            target.add(f.getPosition());
        target.div(me.getEye().getFarSight().size() + 1);

        return me.getToroidalDistanceVector(target);
    }
}