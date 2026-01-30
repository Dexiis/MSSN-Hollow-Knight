package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Implementa o comportamento de Separação (Separation) para agentes de bando.
 * <p>
 * Este comportamento evita colisões e sobrelotação, gerando uma força de repulsão
 * que afasta a entidade dos vizinhos que estejam demasiado próximos.
 * </p>
 */
public class Separate extends Behaviour {

    /**
     * Constrói uma nova instância do comportamento de Separação.
     *
     * @param weight o peso de influência deste comportamento na decisão final de movimento
     */
    public Separate(float weight) {
        super(weight);
    }

    /**
     * Calcula o vetor de repulsão para afastar a entidade dos vizinhos imediatos.
     * <p>
     * Para cada vizinho na zona de visão próxima, calcula um vetor oposto à sua posição.
     * A magnitude da repulsão é inversamente proporcional à distância (quanto mais perto,
     * mais forte é a força de afastamento).
     * </p>
     *
     * @param me a entidade do bando que está a ser atualizada
     * @return o vetor de velocidade desejada para evitar colisão
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = new PVector();
        for (Flock f : me.getEye().getNearSight()) {
            PVector direction = me.getToroidalDistanceVector(f.getPosition()).mult(-1);
            float dir = direction.mag();
            direction.div(dir * dir);
            desiredVelocity.add(direction);
        }

        return desiredVelocity;
    }
}