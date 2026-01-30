package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Implementa o comportamento de Alinhamento (Alignment) para agentes de bando.
 * <p>
 * Este comportamento dita que uma entidade deve tentar mover-se na mesma direção
 * e velocidade média que os seus vizinhos locais, promovendo um movimento de grupo coordenado.
 * </p>
 */
public class Align extends Behaviour {

    /**
     * Constrói uma nova instância do comportamento de Alinhamento.
     *
     * @param weight o peso de influência deste comportamento na decisão final de movimento
     */
    public Align(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada baseada na média das velocidades dos vizinhos.
     * <p>
     * Itera sobre todas as entidades visíveis no alcance distante, soma as suas velocidades
     * e calcula a média para determinar a direção de fluxo do grupo.
     * </p>
     *
     * @param me a entidade do bando que está a ser atualizada
     * @return o vetor de velocidade média do grupo vizinho
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = me.getVelocity().copy();
        for (Flock character : me.getEye().getFarSight())
            desiredVelocity.add(character.getVelocity());

        return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
    }
}